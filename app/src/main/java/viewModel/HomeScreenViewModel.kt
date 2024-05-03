package viewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.ImagePainter
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import firebase.FirebaseRegistrations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeScreenViewModel():ViewModel() {

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





}
