package viewModel


import android.Manifest
import android.content.Context
import android.util.Log
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diplom1.R
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.util.UUID

class VideoCallViewModel : ViewModel() {
    val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    val requestCode = 1
    val userID = mutableStateOf("")
    val saveID = mutableStateOf("")
    val caller = mutableStateOf("")
    val audio = mutableStateOf(true)
    val video = mutableStateOf(true)
    val isPeerConnected = mutableStateOf(false)
    val imageAudio = mutableStateOf(R.drawable.baseline_mic_24)
    val imageVideo = mutableStateOf(R.drawable.baseline_videocam_24)
    val firebaseRef = Firebase.database.getReference("users")
    var webView: WebView? = null
    val isAudioEnabled = mutableStateOf(true)
    val isVideoEnabled = mutableStateOf(true)
    val unicId = mutableStateOf("")

    private val _incomingCall = MutableLiveData<Boolean>()
    val incomingCall: LiveData<Boolean> get() = _incomingCall

    init {
        userID.value = getUnitID()
    }

    fun setupWebView(webView: WebView, context: Context) {
        this.webView = webView
        Log.d("Видеозвонок", "Настройка WebView")
        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                request?.grant(request.resources)
                Log.d("Видеозвонок", "Разрешения для WebView предоставлены")
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        loadVideoCall()
    }

    fun loadVideoCall() {
        val filePath = "file:android_asset/call.html"
        Log.d("Видеозвонок", "Загрузка HTML видеозвонка из $filePath")
        webView?.loadUrl(filePath)

        webView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d("Видеозвонок", "Страница WebView завершила загрузку")
                initializePeers()
            }
        }
    }

    private fun initializePeers() {
        unicId.value = getUnitID()
        Log.d("Видеозвонок", "Инициализация пиров с ID ${unicId.value}")
        callJavascriptFunction("javascript:init(\"${unicId.value}\")")

        Log.d("Видеозвонок", "Установка слушателя для входящих звонков для userID: ${userID.value}")
        firebaseRef.child(userID.value).child("incoming").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val callValue = snapshot.value as? String
                    Log.d("Видеозвонок", "Данные о входящем звонке изменены: $callValue")
                    onCallRequest(callValue)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(
                        "Видеозвонок",
                        "Ошибка при прослушивании входящих звонков: ${error.message}"
                    )
                }
            }
        )
    }

    private fun onCallRequest(call: String?) {
        Log.d("Видеозвонок", "Получен запрос на звонок: $call")
        caller.value = call ?: return
        _incomingCall.postValue(true)
    }

    fun callJavascriptFunction(functionString: String) {
        Log.d("Видеозвонок", "Вызов JavaScript функции: $functionString")
        webView?.post { webView?.evaluateJavascript(functionString, null) }
    }

    fun getUnitID(): String {
        val uuid = UUID.randomUUID().toString()
        Log.d("Видеозвонок", "Сгенерирован уникальный ID: $uuid")
        return uuid
    }

    fun toggleAudio() {
        audio.value = !audio.value
        Log.d("Видеозвонок", "Переключение аудио: ${audio.value}")
        callJavascriptFunction("javascript:toggleAudio(\"${audio.value}\")")
        imageAudio.value = if (audio.value) {
            R.drawable.baseline_mic_24
        } else {
            R.drawable.baseline_mic_off_24
        }
    }

    fun toggleVideo() {
        video.value = !video.value
        Log.d("Видеозвонок", "Переключение видео: ${video.value}")
        callJavascriptFunction("javascript:toggleVideo(\"${video.value}\")")
        imageVideo.value = if (video.value) {
            R.drawable.baseline_videocam_24
        } else {
            R.drawable.baseline_videocam_off_24
        }
    }

    fun sendCallRequest(context: Context, friendUsername: String) {
        if (!isPeerConnected.value) {
            Toast.makeText(context, "Пир не подключен. Проверьте подключение к интернету.", Toast.LENGTH_LONG).show()
            return
        }

        firebaseRef.child(friendUsername).child("incoming").setValue(userID.value)
            .addOnSuccessListener {
                Log.d("Видеозвонок", "Запрос на звонок успешно отправлен")
            }
            .addOnFailureListener { e ->
                Log.e("Видеозвонок", "Ошибка при отправке запроса на звонок: ${e.message}")
            }

        firebaseRef.child(friendUsername).child("isAvailable").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("Видеозвонок", "Ошибка при проверке доступности: ${error.message}")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value.toString() == "true") {
                    listenForConnId(friendUsername)
                } else {
                    Log.d("Видеозвонок", "Пользователь недоступен для вызова")
                }
            }
        })
    }

    private fun listenForConnId(friendUsername: String) {
        firebaseRef.child(friendUsername).child("connId").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("Видеозвонок", "Ошибка при прослушивании ID подключения: ${error.message}")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null) return
                callJavascriptFunction("javascript:startCall(\"${snapshot.value}\")")
            }
        })
    }

    fun handlePermissions(context: Context, permissionsGranted: Boolean) {
        if (!permissionsGranted) {
            Toast.makeText(context, "Необходимо предоставить разрешения на использование камеры и микрофона.", Toast.LENGTH_LONG).show()
        } else {
            webView?.let { loadVideoCall() }
        }
    }

}





/*private val firestore = FirebaseFirestore.getInstance()

fun createCall(roomName: String, callerId: String, receiverId: String) {
    val callData = hashMapOf(
        "roomName" to roomName,
        "callerId" to callerId,
        "receiverId" to receiverId,
        "status" to "calling"
    )
    firestore.collection("calls").add(callData)
}

fun listenForCalls(userId: String, onCallReceived: (String) -> Unit) {
    firestore.collection("calls")
        .whereEqualTo("receiverId", userId)
        .whereEqualTo("status", "calling")
        .addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null || snapshot.isEmpty) return@addSnapshotListener
            for (doc in snapshot.documents) {
                val roomName = doc.getString("roomName") ?: continue
                onCallReceived(roomName)
            }
        }
}
 val uidFriends:MutableState<String> = mutableStateOf("")
fun geyUidFriens(cardVolonterViewModel:CardVolonterViewModel,
                        email:String,
                        userType:UserType,
                         context:Context,
                       //  udFriends:MutableState<String>
) : MutableState<String>{
    viewModelScope.launch {
        val status = ShedPreferences.getUserType(context)
        val nameCollections = if (status == userType.UserBlind.value) {
            NameCollactionFirestore.UsersVolonters
        } else {
            NameCollactionFirestore.UsersBlind
        }
       val ui =  FireBaseIDCardUser().idByEmailSearch(
            email,
            nameCollections = nameCollections,
            uidFriends,
            nameFileInCollectionSearch = FirebaseString.email
        )
    }
    return uidFriends
}
// Function to search user by email
fun idByEmailSearch(
    stringSearch: String,
    context:Context,
    userType: UserType,
    uidByStringSave: MutableState<String>,
    nameFileInCollectionSearch: String,
    //onResult: (Boolean) -> Unit
) {
    viewModelScope.launch {
        try {
            val status = ShedPreferences.getUserType(context)
            val nameCollections = if (status == userType.UserBlind.value) {
                NameCollactionFirestore.UsersVolonters
            } else {
                NameCollactionFirestore.UsersBlind
            }
            if (stringSearch.isNotEmpty()) {
                val query = firestore.collection(nameCollections)
                    .whereEqualTo(nameFileInCollectionSearch, stringSearch)
                    .get()
                    .await()
                if (!query.isEmpty) {
                    val uid = query.documents[0].id
                    uidByStringSave.value = uid
                    Log.d("call", "Found UID: $uid")
                  //  onResult(true)
                } else {
                    Log.e("call", "Document not found")
                   // onResult(false)
                }
            }
        } catch (e: Exception) {
            Log.e("call", "Error: ${e.message}")
        //    onResult(false)
        }
    }
}
}
*/
/*fun generateAgoraToken(
    appID: String,
    appCertificate: String,
    channelName: String,
    uid: Int,
    role: String,
    expirationTimeInSeconds: Int
): String {
    val cx: Context = Context.enter()
    cx.optimizationLevel = -1

    try {
        val scope: Scriptable = cx.initStandardObjects()

        // Считываем JavaScript файл
        cx.evaluateString(
            scope, """
        const Agora = require('agora-access-token');

        function generateToken(appID, appCertificate, channelName, uid, role, expirationTimeInSeconds) {
            const currentTimestamp = Math.floor(Date.now() / 1000);
            const privilegeExpiredTs = currentTimestamp + expirationTimeInSeconds;

            const token = Agora.RtcTokenBuilder.buildTokenWithUid(appID, appCertificate, channelName, uid, role, privilegeExpiredTs);
            return token;
        }
    """.trimIndent(), "generateToken.js", 1, null
        )

        // Вызываем функцию generateToken из JavaScript
        val generateTokenFunction =
            scope.get("generateToken", scope) as org.mozilla.javascript.Function
        val result = generateTokenFunction.call(
            cx,
            scope,
            scope,
            arrayOf(appID, appCertificate, channelName, uid, role, expirationTimeInSeconds)
        )
        return result as String
    } finally {
        Context.exit()
    }
}*/



/*    fun videoCallServiceUser(UserID:MutableState<String>, context: Context){
        val  application =  context // Android's application context
        val appID:Long= 1256948071;   // yourAppID
        val appSign = "f01e980e182ac07d79c1e7560ee49e8e2c361daad5e79be68c1402a57410c0e7"  // yourAppSign
        String userID =; // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName =;   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig.callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

        ZegoUIKitPrebuiltCallService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);
    }*//*

}*/



/*private val firestore = FirebaseFirestore.getInstance()

    fun createCall(roomName: String, callerId: String, receiverId: String) {
        val callData = hashMapOf(
            "roomName" to roomName,
            "callerId" to callerId,
            "receiverId" to receiverId,
            "status" to "calling"
        )
        firestore.collection("calls").add(callData)
    }

    fun listenForCalls(userId: String, onCallReceived: (String) -> Unit) {
        firestore.collection("calls")
            .whereEqualTo("receiverId", userId)
            .whereEqualTo("status", "calling")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null || snapshot.isEmpty) return@addSnapshotListener
                for (doc in snapshot.documents) {
                    val roomName = doc.getString("roomName") ?: continue
                    onCallReceived(roomName)
                }
            }
    }
     val uidFriends:MutableState<String> = mutableStateOf("")
    fun geyUidFriens(cardVolonterViewModel:CardVolonterViewModel,
                            email:String,
                            userType:UserType,
                             context:Context,
                           //  udFriends:MutableState<String>
    ) : MutableState<String>{
        viewModelScope.launch {
            val status = ShedPreferences.getUserType(context)
            val nameCollections = if (status == userType.UserBlind.value) {
                NameCollactionFirestore.UsersVolonters
            } else {
                NameCollactionFirestore.UsersBlind
            }
           val ui =  FireBaseIDCardUser().idByEmailSearch(
                email,
                nameCollections = nameCollections,
                uidFriends,
                nameFileInCollectionSearch = FirebaseString.email
            )
        }
        return uidFriends
    }
    // Function to search user by email
    fun idByEmailSearch(
        stringSearch: String,
        context:Context,
        userType: UserType,
        uidByStringSave: MutableState<String>,
        nameFileInCollectionSearch: String,
        //onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val status = ShedPreferences.getUserType(context)
                val nameCollections = if (status == userType.UserBlind.value) {
                    NameCollactionFirestore.UsersVolonters
                } else {
                    NameCollactionFirestore.UsersBlind
                }
                if (stringSearch.isNotEmpty()) {
                    val query = firestore.collection(nameCollections)
                        .whereEqualTo(nameFileInCollectionSearch, stringSearch)
                        .get()
                        .await()
                    if (!query.isEmpty) {
                        val uid = query.documents[0].id
                        uidByStringSave.value = uid
                        Log.d("call", "Found UID: $uid")
                      //  onResult(true)
                    } else {
                        Log.e("call", "Document not found")
                       // onResult(false)
                    }
                }
            } catch (e: Exception) {
                Log.e("call", "Error: ${e.message}")
            //    onResult(false)
            }
        }
    }
}
*//*

    */
/*fun generateAgoraToken(
        appID: String,
        appCertificate: String,
        channelName: String,
        uid: Int,
        role: String,
        expirationTimeInSeconds: Int
    ): String {
        val cx: Context = Context.enter()
        cx.optimizationLevel = -1

        try {
            val scope: Scriptable = cx.initStandardObjects()

            // Считываем JavaScript файл
            cx.evaluateString(
                scope, """
            const Agora = require('agora-access-token');

            function generateToken(appID, appCertificate, channelName, uid, role, expirationTimeInSeconds) {
                const currentTimestamp = Math.floor(Date.now() / 1000);
                const privilegeExpiredTs = currentTimestamp + expirationTimeInSeconds;

                const token = Agora.RtcTokenBuilder.buildTokenWithUid(appID, appCertificate, channelName, uid, role, privilegeExpiredTs);
                return token;
            }
        """.trimIndent(), "generateToken.js", 1, null
            )

            // Вызываем функцию generateToken из JavaScript
            val generateTokenFunction =
                scope.get("generateToken", scope) as org.mozilla.javascript.Function
            val result = generateTokenFunction.call(
                cx,
                scope,
                scope,
                arrayOf(appID, appCertificate, channelName, uid, role, expirationTimeInSeconds)
            )
            return result as String
        } finally {
            Context.exit()
        }
    }*//*




*/
/*    fun videoCallServiceUser(UserID:MutableState<String>, context: Context){
        val  application =  context // Android's application context
        val appID:Long= 1256948071;   // yourAppID
        val appSign = "f01e980e182ac07d79c1e7560ee49e8e2c361daad5e79be68c1402a57410c0e7"  // yourAppSign
        String userID =; // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName =;   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig.callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

        ZegoUIKitPrebuiltCallService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);
    }*//*
*/
/*

}*/

