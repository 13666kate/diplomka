package viewModel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
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
import com.example.diplom1.ui.theme.Red
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date

class RegistrationViewModel : ViewModel() {
 //   lateinit var auth: FirebaseAuth
   // lateinit var firestore: FirebaseFirestore

    // lateinit var  context:Context
   //  val toast = Toast.makeText(context,"Не правильно заолненно поле", Toast.LENGTH_SHORT).show()
    var _isClickedLabel = mutableStateOf(false)
    private val _isClickedLabel2 = mutableStateOf(false)
    private val _isClickedIcon = mutableStateOf(false)
    private val _iconImageVector = mutableStateOf<ImageVector?>(null)
    val iconImageVector: State<ImageVector?> = _iconImageVector
    val openDateDialog = mutableStateOf(false)
    val falsed = mutableStateOf(false)
    val registrationsButtonVisability = mutableStateOf(false)

    val isCameraPermission = mutableStateOf(false)
    val faceDetections = mutableStateOf(false)

    //для OCR
    val _imageBitmapTextRecognId = mutableStateOf<Bitmap?>(null)
    val imageBitmapTextRecognId: MutableState<Bitmap?> = _imageBitmapTextRecognId

    val _imageBitmapTextRecognPin = mutableStateOf<Bitmap?>(null)
    val imageBitmapTextRecognPin: MutableState<Bitmap?> = _imageBitmapTextRecognPin

    //для распознавания лица
    val _imageBitmapFaceRecogn = mutableStateOf<Bitmap?>(null)
    val imageBitmapFaceRecogn: MutableState<Bitmap?> = _imageBitmapFaceRecogn


    val login = mutableStateOf("")
    val password = mutableStateOf("")
    val email = mutableStateOf("")
    val lastName = mutableStateOf("")
    val festName = mutableStateOf("")
    val number = mutableStateOf("")
    val birthday = mutableStateOf("")
    val textOrRecognezedId = mutableStateOf("")
    val textOrRecognezedPin = mutableStateOf("")
    val errorTextFaceDetectionsId = mutableStateOf("Фото с паспорта ")
    val errorTextFaceDetectionsPhoto = mutableStateOf("Фото для подтверждения")


    var textColorLogin: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorPassword: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorLabelEmail: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorLabelLastName: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorLabelFestName: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorLabelNumber: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textLabelColor: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textLabelColorDate: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textLabelColorIDCard: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textLabelColorPinCard: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))

    var textLabelColorClick: MutableState<Color> =
        mutableStateOf(Orange)//устанавливаем цвет при нажатии на ввод
    var textColorDetectionsIdCard: MutableState<Color> =
        mutableStateOf(colorOlivical) //устанавливаем цвет при нажатии на ввод
    var textColorDetectionsPhoto: MutableState<Color> =
        mutableStateOf(colorOlivical) //устанавливаем цвет при нажатии на ввод

    val _imageUriState = mutableStateOf<Uri?>(null)
    val imageUriState: State<Uri?> = _imageUriState
    var iconColor: MutableState<Color> = mutableStateOf(Black)
    val isButtonEnabled = mutableStateOf(false)
    fun setLabelColor(color: Color) {

        textColorLogin.value = color
    }

    fun setImageUri(uri: Uri?) {
        _imageUriState.value = uri
    }

    fun setIconColor() {
        //_isClicked.value = true
        iconColor.value = if (iconColor.value == Black) Color(Gray.value) else Color(Black.value)
    }

    fun setIconImage(uri: Uri?) {
        if (_imageUriState.value != null) {
            _imageUriState.value = uri
        }
    }

    fun cluePinCard() {
        if (textOrRecognezedPin.value.length == 14 && textOrRecognezedPin.value.all { it.isDigit() }) {
            textLabelColorPinCard.value = colorOlivical
        } else {
            textLabelColorPinCard.value = Red
            isButtonEnabled.value = false

        }
    }

    fun clueLogin(
        textEmail: MutableState<String>,
        textColor: MutableState<Color>
    ) {
        var text = textEmail.value
        val containsUpperCase = !text.any { it.isUpperCase() }
        if (textEmail.value.isEmpty() || (textEmail.value.contains(' ') || containsUpperCase)) {
            textColor.value = Red
        } else {
            textColor.value = colorOlivical

        }
    }

    //registrationViewModel.isButtonEnabled.value = false
    fun cluePassword(
        textPassword: MutableState<String>,
        textColor: MutableState<Color>
    ) {

        var text = textPassword.value
        val containsNumber = !text.any { it.isDigit() }
        if (textPassword.value.isEmpty() || containsNumber) {
            textColor.value = Red

        } else {
            textColor.value = colorOlivical
        }

    }

    fun clueEmail(
        textEmail: MutableState<String>,
        textColor: MutableState<Color>
    ) {

        var text = textEmail.value
        val containsNumber = !text.contains('@')
        val endsWithWord = !text.endsWith("@gmail.com")
        if (textEmail.value.isEmpty() || endsWithWord) {
            textColor.value = Red
        } else {
            textColor.value = colorOlivical
        }
        //registrationViewModel.isButtonEnabled.value = false
    }

    fun clueLastName(
        textLastName: MutableState<String>,
        textColor: MutableState<Color>
    ) {

        var text = textLastName.value
        val containsUpperCase = !text.any { it.isUpperCase() }
        val containsNumber = text.any { it.isDigit() }
        val containsSymbol = text.any { !it.isLetterOrDigit() }

        if (textLastName.value.isEmpty() || containsNumber || containsSymbol || !text[0].isUpperCase()) {
            textColor.value = Red
        } else {

            textColor.value = colorOlivical
        }
        //registrationViewModel.isButtonEnabled.value = false
    }

    fun clueNumber(
        textNumber: MutableState<String>,
        textColor: MutableState<Color>
    ) {

        var text = textNumber.value
        val isCorrectFormat = checkPhoneNumberFormat(text)
        if (textNumber.value.isEmpty() || !isCorrectFormat) {
            textColor.value = Red

        } else {
            textColor.value = colorOlivical

        }
        //registrationViewModel.isButtonEnabled.value = false
    }

    fun clueDate(
        dataState: MutableState<String>,
        textColorDate: MutableState<Color>
    ) {

        var text = dataState.value
        val isCorrectFormat = checkPhoneNumberFormat(text)
        if (dataState.value.isEmpty() || !isValidDateFormat(text)) {
            textColorDate.value = Red

        } else {
            textColorDate.value = colorOlivical

        }
        //registrationViewModel.isButtonEnabled.value = false
    }
    fun clueIdCard( state: MutableState<String>,
                   textColorIdCard: MutableState<Color>){
        val regex = "\\bID\\d+\\b".toRegex()
        if(regex.matches(state.value) && state.value.length== 9){
            textColorIdCard.value = colorOlivical

            }else{
            textColorIdCard.value = Red

        }
    }

    fun checkPhoneNumberFormat(phoneNumber: String): Boolean {
        val regex = Regex("^\\+996\\d{9}$")
        return regex.matches(phoneNumber)
    }

    fun isValidDateFormat(text: String): Boolean {
        val regex = Regex("^\\d{1,2}\\.\\d{1,2}\\.\\d{4}\$")
        //проверка верен ли шаблон
        if (!regex.matches(text)) {
            return false
        }
        // разбиваем нашу строку на части
        // ориентируемся точкой
        val parts = text.split(".")
        val day = parts[0].toIntOrNull()
        val month = parts[1].toIntOrNull()
        val year = parts[2].toIntOrNull()

        // Проверка корректности значений дня, месяца и года
        //не пусты ли значения
        if (day == null || month == null || year == null) {
            return false
        }
        // проверка  день меясц и год  в правильном диапозоне
        if (day !in 1..31 || month !in 1..12 || year < 0) {

            return false
        }
        // если это февраль то проверка на высокосный год
        val maxDay = when (month) {
            2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            // апрель июнь сентябрь ноябрь имеют по 30 дней
            4, 6, 9, 11 -> 30
            // все остальные по 31
            else -> 31
        }
        if (day !in 1..maxDay) {
            return false
        }

        return true
    }


    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("d.MM.yyyy")
        return format.format(date)
    }



}

