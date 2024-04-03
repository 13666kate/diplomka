package viewModel

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.ui.theme.Orange
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.diplom1.ui.theme.Black

class RegistrationViewModel: ViewModel() {

    var _isClickedLabel = mutableStateOf(false)
    private val _isClickedLabel2 = mutableStateOf(false)
    private val _isClickedIcon = mutableStateOf(false)
    private val _iconImageVector = mutableStateOf<ImageVector?>(null)
    val iconImageVector: State<ImageVector?> = _iconImageVector
    val openDateDialog = mutableStateOf(false)

    val login = mutableStateOf("")
    val password = mutableStateOf("")
    val email = mutableStateOf("")
    val lastName = mutableStateOf("")
    val festName = mutableStateOf("")
    val number = mutableStateOf("")
    val birthday  = mutableStateOf("")


     var textColorLogin: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
     var textColorPassword: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
     var textColorLabelEmail: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
     var textColorLabelLastName: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
     var textColorLabelFestName: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
     var textColorLabelNumber: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textLabelColor: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textLabelColorDate: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textLabelColorClick: MutableState<Color> = mutableStateOf(Orange)//устанавливаем цвет при нажатии на ввод
    var textColor: MutableState<Color> = mutableStateOf(colorOlivical) //устанавливаем цвет при нажатии на ввод

    val _imageUriState  = mutableStateOf<Uri?>(null)
    val imageUriState: State<Uri?> = _imageUriState
    var iconColor: MutableState<Color> = mutableStateOf(Black)
    val isButtonEnabled = mutableStateOf(true)
    fun setLabelColor(color: Color)  {

           textColorLogin.value =color
    }
    fun setImageUri(uri: Uri?) {
        _imageUriState.value = uri
    }
    fun setIconColor() {
        //_isClicked.value = true
       iconColor.value = if (iconColor.value == Black) Color(Gray.value) else Color(Black.value)
    }
    fun setIconImage(uri: Uri?) {
       if( _imageUriState.value != null) {
           _imageUriState.value = uri
       }

    }
}

