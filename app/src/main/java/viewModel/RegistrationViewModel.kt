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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.diplom1.R
import com.example.diplom1.ui.theme.Black
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.Red
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import firebase.FirebaseRegistrations
import firebase.NameCollactionFirestore
import kotlinx.coroutines.launch
import screen.storage
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

    val expandedListState = mutableStateOf(false) //для состояния листа
    val nameOrganizationList = listOf("Красный полумесяц", "Красный крест ", "Молодежный цент")
    val nameRegionList = listOf(
        "Чуйская область ",
        "Иссык-Кульская",
        "Нарынская",
        "Талаская",
        "Баткенская",
        "Джалал-Абадская",
        "Ошская",

    )
    val nameRayonList = listOf(
        "Лейлекский", "Кадамжайский ", "Баткенский ",
        "Аксыйский", "Ала-Букинский", "Базар-Коргонский",
        "Ноокенский","Сузакский район","Жети-Огузский",
        "Чаткальский ","Ак-Суйский","Иссык-Кульский",
        "Тонский ","Тюпский","Ак-Талинский",
        "Ат-Башинский ","Жумгальский","Араванский",
        "Кара-Кульджинский ","Кара-Сууский","Ноокатский",
        "Узгенский ","Чон-Алайский"," Бакай-Атинский",
        "Айтматовский","Манасский"," Таласский",
        "Аламудунский","Жайыльский","Кеминский",
        "Московский","Панфиловский","Сокулукский",
        "Ыссык-Атинский"," Чуйский район"
        )

    val textList = mutableStateOf("")
    val textRegionAsList = mutableStateOf("")
    val textRayonAsList = mutableStateOf("")
    var textSize = mutableStateOf(Size.Zero)

    val login = mutableStateOf("")
    val password = mutableStateOf("")
    val email = mutableStateOf("")
    val lastName = mutableStateOf("")
    val festName = mutableStateOf("")
    val number = mutableStateOf("")
    val birthday = mutableStateOf("")
    val statusInvalid = mutableStateOf("")
    val aboutMe = mutableStateOf("")
    val organizationVolonter = mutableStateOf("")
    val region = mutableStateOf("")
    val experienceVolonter=mutableStateOf("")
    val rayon = mutableStateOf("")
    val adress = mutableStateOf("")

    val textOrRecognezedId = mutableStateOf("")
    val textOrRecognezedPin = mutableStateOf("")
    val errorTextFaceDetectionsId = mutableStateOf("Фото с паспорта ")
    val errorTextFaceDetectionsPhoto = mutableStateOf("Фото для подтверждения")

    val isButtonEnabledVolonters=mutableStateOf(false)
    var textColorLogin: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorList: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorListRayon: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorListRegion: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorAdress: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorAboutMe: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorExperienceVolonter: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorPassword: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorLabelEmail: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorLabelLastName: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorLabelFestName: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textColorLabelNumber: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textLabelColor: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textLabelColorDate: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textLabelColorIDCard: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var textLabelColorPinCard: MutableState<Color> = mutableStateOf(Color(colorOlivical.value))
    var colorEnabledValueButtonRegistrations: MutableState<Color> =
        mutableStateOf(Color(colorOlivical.value))

    var colorEnabledValueButtonRegistrationsVolo: MutableState<Color> =
        mutableStateOf(Color(colorOlivical.value))
    var bagroungColorDiplom: MutableState<Color> = mutableStateOf(BlueBlack)
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
    val imageUriString = mutableStateOf("")
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

    fun cluePinCard(): Boolean {
        if (textOrRecognezedPin.value.length == 14 && textOrRecognezedPin.value.all { it.isDigit() }) {
            textLabelColorPinCard.value = colorOlivical
            return true
        } else {
            textLabelColorPinCard.value = Red
            return false

        }
    }

    fun clueLogin(
        textEmail: MutableState<String>,
        textColor: MutableState<Color>
    ): Boolean {
        var text = textEmail.value
        val containsUpperCase = !text.any { it.isUpperCase() }
        if (textEmail.value.isEmpty() || (textEmail.value.contains(' ') || containsUpperCase)) {
            textColor.value = Red
            return false
        } else {
            textColor.value = colorOlivical
            return true
        }
    }

    //registrationViewModel.isButtonEnabled.value = false
    fun cluePassword(
        textPassword: MutableState<String>,
        textColor: MutableState<Color>
    ): Boolean {

        var text = textPassword.value
        val containsNumber = !text.any { it.isDigit() }
        if (textPassword.value.isEmpty() || containsNumber) {
            textColor.value = Red
            return false

        } else {
            textColor.value = colorOlivical
            return true
        }

    }
     fun clueEboutMe():Boolean{
         if (aboutMe.value.isNotEmpty() && aboutMe.value.length<20){
             textColorAboutMe.value = colorOlivical
             return true
         }else{
             textColorAboutMe.value = Red
             return false
         }
     }

    fun clueEmail(
        textEmail: MutableState<String>,
        textColor: MutableState<Color>
    ): Boolean {

        var text = textEmail.value
        val containsNumber = !text.contains('@')
        val endsWithWord = !text.endsWith("@gmail.com")
        if (textEmail.value.isEmpty() || endsWithWord) {
            textColor.value = Red
            return false
        } else {
            textColor.value = colorOlivical
            return true
        }
        //registrationViewModel.isButtonEnabled.value = false
    }

    fun clueLastName(
        textLastName: MutableState<String>,
        textColor: MutableState<Color>
    ): Boolean {

        var text = textLastName.value
        val containsUpperCase = !text.any { it.isUpperCase() }
        val containsNumber = text.any { it.isDigit() }
        val containsSymbol = text.any { !it.isLetterOrDigit() }

        if (textLastName.value.isEmpty() || containsNumber || containsSymbol || !text[0].isUpperCase()) {
            textColor.value = Red
            return false
        } else {
            textColor.value = colorOlivical
            return true
        }
        //registrationViewModel.isButtonEnabled.value = false
    }

    fun clueNumber(
        textNumber: MutableState<String>,
        textColor: MutableState<Color>
    ): Boolean {

        var text = textNumber.value
        val isCorrectFormat = checkPhoneNumberFormat(text)
        if (textNumber.value.isEmpty() || !isCorrectFormat) {
            textColor.value = Red
            return false

        } else {
            textColor.value = colorOlivical
            return true

        }
        //registrationViewModel.isButtonEnabled.value = false
    }

    fun clueDate(
        dataState: MutableState<String>,
        textColorDate: MutableState<Color>
    ): Boolean {

        var text = dataState.value
        val isCorrectFormat = checkPhoneNumberFormat(text)
        if (dataState.value.isEmpty() || !isValidDateFormat(text)) {
            textColorDate.value = Red
            return false

        } else {
            textColorDate.value = colorOlivical
            return true
        }
        //registrationViewModel.isButtonEnabled.value = false
    }

    fun clueIdCard(
        state: MutableState<String>,
        textColorIdCard: MutableState<Color>
    ): Boolean {
        val regex = "\\bID\\d+\\b".toRegex()
        if (regex.matches(state.value) && state.value.length == 9) {
            textColorIdCard.value = colorOlivical
            return true

        } else {
            textColorIdCard.value = Red
            return false

        }
    }

    fun clueList(text:MutableState<String>): Boolean {
        if (text.value.isEmpty()) {
            textColorList.value = Red
            return false
        } else {
            textColorList.value = colorOlivical
            return true
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
    fun clueList(text:MutableState<String>,
                 list:List<String>,
                 textColorList:MutableState<Color>):Boolean{
        if (text.value.isEmpty()) {
            textColorList.value = Red
            return false
        } else {
            for (index in list) {
                if (text.value.contains(index)) {
                    textColorList.value = colorOlivical
                    return true
                }
            }
            // Если текст не содержится в списке, устанавливаем цвет текста на красный
            textColorList.value = Red
            return false
        }
    }

    fun clueExperienceVolonter():Boolean{
        val regex = Regex("[0-9]+")
        val experience = experienceVolonter.value
        if (experience.isNullOrEmpty()) {
            textColorExperienceVolonter.value = Red
            return false
        }

        if (regex.matches(experience)) {
            val intYears = experience.toInt()
            if (intYears in 0..29 && experience.length <= 2) {
                textColorExperienceVolonter.value = colorOlivical
                return true
            }
        }

        textColorExperienceVolonter.value = Red
        return false
    }
    fun clueAdress():Boolean{
        if(adress.value.isNotEmpty()){
            textColorAdress. value = colorOlivical
            return true
        }else{
            textColorAdress. value = Red
            return false
        }
    }

    fun cleareState() {
        login.value = ""
        password.value = ""
        email.value = ""
        _imageUriState.value = null
        lastName.value = ""
        festName.value = ""
        birthday.value = ""
        textOrRecognezedId.value = ""
        textOrRecognezedPin.value = ""
        number.value = ""
        aboutMe.value = ""
        statusInvalid.value = ""
        textList.value = ""
        organizationVolonter.value = ""
        rayon.value = ""
        region.value = ""
        experienceVolonter.value = ""
        adress.value =""



    }

    fun userRegistrations(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        context: Context,
        login: String,
        email: String,
        password: String,
        documentName: String,
        registrationViewModel: RegistrationViewModel,


        ) {
        viewModelScope.launch() {
            FirebaseRegistrations().registerUser(
                auth = auth,
                firestore = firestore,
                storage = storage,
                context = context,
                name = festName.value,
                surname = lastName.value,
                email = email,
                password = password,
                imageUri = imageUriState.value,
                login = login,
                documentName = documentName,
                pinCard = textOrRecognezedPin.value,
                idCard = textOrRecognezedId.value,
                registrationViewModel = registrationViewModel,
                birdhday = birthday.value,
                phone = number.value,
                organizationVolonter = organizationVolonter.value,
                statusInvalid = statusInvalid.value,
                aboutMe = aboutMe.value,
                adress = adress.value,
                region = region.value,
                volonterExperience = experienceVolonter.value,
                rayon = rayon.value


            )
        }
    }
}



