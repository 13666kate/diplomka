package viewModel

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diplom1.R
import com.example.diplom1.ShedPreferences
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import firebase.FireBaseIDCardUser
import firebase.FirebaseRegistrations
import firebase.FirebaseString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Locale

class HomeScreenViewModel():ViewModel(), LifecycleObserver{

    val _imageUriState = mutableStateOf<Uri?>(null)
    val imageUriState: State<Uri?> = _imageUriState
    val uidUserState  = mutableStateOf<String?>(null)
    val imageStateAccountAvatar  = mutableStateOf<ImageVector>(Icons.Default.AccountCircle)
    val imageState  = mutableStateOf<Bitmap?>(null)
   // val imageStateHome  = mutableStateOf<ImageBitmap>(TODO())
    val login = mutableStateOf("")
    val password = mutableStateOf("")
    val email = mutableStateOf("")
    val name = mutableStateOf("")
    val surname = mutableStateOf("")
    val number = mutableStateOf("")
    val birthday = mutableStateOf("")
    val textOrRecognezedId = mutableStateOf("")
    val textOrRecognezedPin = mutableStateOf("")
    private val _iconImageVector = mutableStateOf<ImageVector?>(null)
     var mediaPlayer: MediaPlayer? = null
     var isPlaying = mutableStateOf(false)
     val requestSos = mutableStateOf(false)
    val idList = mutableListOf<String>()
    // Состояние для управления видимостью блокирующего экрана
     val _isBlockingScreenVisible = mutableStateOf(false)
    val isBlockingScreenVisible: State<Boolean> get() = _isBlockingScreenVisible
     //val ListIDUser = mutableListOf<String>()
       // private set

    fun initMediaPlayer(context: Context,lifecycleOwner: LifecycleOwner) {
        mediaPlayer = MediaPlayer.create(context, R.raw.sirena)
        lifecycleOwner.lifecycle.addObserver(this)
    }
    fun start() {
        viewModelScope.launch(Dispatchers.Main) {
            mediaPlayer?.let {
                if (!isPlaying.value) {
                    it.start()
                    isPlaying.value = true
                }
            }
        }
    }

    fun stop() {
        viewModelScope.launch(Dispatchers.Main) {
            mediaPlayer?.let {
                if (isPlaying.value) {
                    it.stop()
                    it.prepare() // Prepare again if you want to play again later
                    isPlaying.value = false
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    // Метод для добавления элемента в список
    @RequiresApi(Build.VERSION_CODES.O)
    fun requestSos() {
        viewModelScope.launch {
            try {
            if (requestSos.value) {
                val id = FirebaseRegistrations().userID().toString()
                val request = Firebase.firestore.collection(FirebaseString.requestSos)
                val currentTime = Calendar.getInstance().time.toString()
                val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = dateFormat.format(Calendar.getInstance().time)
                val data = hashMapOf(
                    FirebaseString.idUser to id,
                    FirebaseString.requestStatusSos to requestSos.value,
                    FirebaseString.currentTime to currentTime,
                    FirebaseString.date to date

                    )
                request.add(data)
                    .addOnSuccessListener { documentReference ->
                        Log.e("Firebase", "Документ добавлен: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Ошибка добавления документа ", e)
                    }
            }

        }catch (e:Exception){
          Log.e("Firebase", "Ошибка"+ e.message.toString())
        }

        }

    }
    suspend fun requestSaveVolontrs(context: Context,
                            userType: UserType,
                            cardVolonterViewModel: CardVolonterViewModel):MutableList<String> {

        val idListp = mutableListOf<String>()

            try {
               val status =ShedPreferences.getUserType(
                   context
               )
                if (status == userType.UserVolonters.value) {
                    val list = FireBaseIDCardUser().getFriendList(
                        context = context,
                        userType = userType,
                        cardVolonterViewModel = cardVolonterViewModel
                    )
                    for (intex in list ) {
                        idListp.addAll( cardVolonterViewModel.idByEmailSearchList(
                            context = context,
                            userType = userType,
                            nameFileInCollectionSearch = FirebaseString.email,
                            stringSearch = intex.email)
                        )
                        Log.e("kop", "list : " + "${idListp}" )
                    }
                }
        }catch (e:Exception){
                Log.e("Firebase", "Ошибка : ", e)

            }

        Log.e("Firebase", "idList : " + "${idList}" )
        return idListp
    }

    suspend fun responseVolonters(
        context: Context,
        onclick :()->Unit,
        userType: UserType,
        cardVolonterViewModel: CardVolonterViewModel){
            try {
            val list: List<String> = requestSaveVolontrs(
                context= context,
                userType = userType,
                cardVolonterViewModel = cardVolonterViewModel)
            if (list.isNotEmpty()) {
                 FirebaseFirestore.getInstance().collection(FirebaseString.requestSos)
                        .whereIn(FirebaseString.idUser, list)
                        .get()
                        .await()
                    _isBlockingScreenVisible.value = true
                    requestSos.value=true
                    onclick()
                    Log.e("Firebase", "Все ок : ")
                }
        }catch (e:Exception){
                Log.e("Firebase", "Ошибка : ", e)

            }        }
    }

