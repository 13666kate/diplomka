package viewModel


import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diplom1.ShedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import firebase.FireBaseIDCardUser
import firebase.FirebaseString
import firebase.NameCollactionFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class VideoCallViewModel(): ViewModel() {

    val userID = mutableStateOf("")





    private val firestore = FirebaseFirestore.getInstance()

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
