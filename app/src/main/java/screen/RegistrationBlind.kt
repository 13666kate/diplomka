@file:Suppress("UNUSED_EXPRESSION")

package screen

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.diplom1.R
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.Grey
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.uiComponets.ComponetsRegistrations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import firebase.FirebaseRegistrations
import firebase.NameCollactionFirestore
import sence.kate.practica3.padding.Padding
import viewModel.RegistrationViewModel
import viewModel.UserType

val componetsRegistrations = ComponetsRegistrations()

val firestore = FirebaseFirestore.getInstance()
val storage = FirebaseStorage.getInstance()

@Composable
fun RegistrationBlind(
    registrationViewModel: RegistrationViewModel,
    onClickNavigate: () -> Unit,
    context: Context,
    userType: UserType
) {
    val clueLogin = registrationViewModel.clueLogin(
        textEmail = registrationViewModel.login,
        textColor = registrationViewModel.textColorLogin
    )

    val cluePassword = registrationViewModel.cluePassword(
        registrationViewModel.password,
        registrationViewModel.textColorPassword
    )
    val clueName = registrationViewModel.clueLastName(
        registrationViewModel.festName,
        registrationViewModel.textColorLabelFestName
    )
    val clueSurname = registrationViewModel.clueLastName(
        registrationViewModel.lastName,
        registrationViewModel.textColorLabelLastName
    )
    val clueEmail = registrationViewModel.clueEmail(
        registrationViewModel.email,
        registrationViewModel.textColorLabelEmail
    )
    val clueNumber = registrationViewModel.clueNumber(
        registrationViewModel.number,
        registrationViewModel.textColorLabelNumber
    )
    val clueDate = registrationViewModel.clueDate(
        registrationViewModel.birthday,
        registrationViewModel.textLabelColorDate
    )

    val clueIdCard = registrationViewModel.clueIdCard(
        state = registrationViewModel.textOrRecognezedId,
        textColorIdCard = registrationViewModel.textLabelColorIDCard
    )
    val cluePinCard = registrationViewModel.cluePinCard()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(BlueBlack.value))
            .verticalScroll(rememberScrollState()),

        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(1) {
            componetsRegistrations.PhotoPicker(
                height = Padding.heightImageRegistrationScreen,
                paddingStart = Padding.paddingLeftOrRightImage,
                paddingTop = Padding.paddingFifty,
                paddingEnd = Padding.paddingLeftOrRightImage,
                paddingBottom = Padding.paddingZero,
                wight = Padding.wightImageRegistrationScreen,
                size = Padding.sizeImageRegistrationScreen,
                viewModel = registrationViewModel,
                sizeAddImage = Padding.iconSizeRegistrationAdd,
                colorImageTrue = colorOlivical,
                setImageUri = { uri ->
                    registrationViewModel.setImageUri(uri)
                },
                imageUriState = registrationViewModel.imageUriState,

            )
            componetsRegistrations.TextField(
                registrationViewModel = registrationViewModel,
                state = registrationViewModel.festName,
                height = Padding.heightOutlineRegistrationsScreen,
                wight = Padding.widthOutlineRegistrationScreen,
                label = R.string.FestName,
                paddingStart = Padding.paddingNormalTen,
                paddingTop = Padding.tvelv,
                paddingEnd = Padding.paddingNormalTen,
                paddingBottom = Padding.paddingNormalTen,
                background = BlueBlack,
                keyboardType = KeyboardType.Password,
                labelColor = registrationViewModel.textColorLabelFestName,
                clueText = R.string.FestNameClue,
                picker = {},
                clueColor = Grey,
                onDoneAction = {
                    clueName
                }
            )
            componetsRegistrations.TextField(
                registrationViewModel = registrationViewModel,
                state = registrationViewModel.lastName,
                height = Padding.heightOutlineRegistrationsScreen,
                wight = Padding.widthOutlineRegistrationScreen,
                label = R.string.LastName,
                paddingStart = Padding.paddingNormalTen,
                paddingTop = Padding.tvelv,
                paddingEnd = Padding.paddingNormalTen,
                paddingBottom = Padding.paddingNormalTen,
                background = BlueBlack,
                keyboardType = KeyboardType.Password,
                labelColor = registrationViewModel.textColorLabelLastName,
                clueText = R.string.LastNameClue,
                picker = {},
                clueColor = Grey,
                onDoneAction = {
                    clueSurname
                }
            )
            componetsRegistrations.TextField(
                registrationViewModel = registrationViewModel,
                state = registrationViewModel.login,
                height = Padding.heightOutlineRegistrationsScreen,
                wight = Padding.widthOutlineRegistrationScreen,
                label = R.string.Login,
                paddingStart = Padding.paddingNormalTen,
                paddingTop = Padding.tvelv,
                paddingEnd = Padding.paddingNormalTen,
                paddingBottom = Padding.paddingNormalTen,
                background = BlueBlack,
                keyboardType = KeyboardType.Email,
                labelColor = registrationViewModel.textColorLogin,
                clueText = R.string.LoginClue,
                clueColor = Grey,
                picker = {},
                onDoneAction = {
                    clueLogin
                }
            )
        }
        componetsRegistrations.TextField(
            registrationViewModel = registrationViewModel,
            state = registrationViewModel.password,
            height = Padding.heightOutlineRegistrationsScreen,
            wight = Padding.widthOutlineRegistrationScreen,
            label = R.string.Password,
            paddingStart = Padding.paddingNormalTen,
            paddingTop = Padding.tvelv,
            paddingEnd = Padding.paddingNormalTen,
            paddingBottom = Padding.paddingNormalTen,
            background = BlueBlack,
            keyboardType = KeyboardType.Password,
            labelColor = registrationViewModel.textColorPassword,
            clueText = R.string.PasswordlClue,
            clueColor = Grey,
            picker = {},
            onDoneAction = {
                cluePassword
            }
        )
        componetsRegistrations.TextField(
            registrationViewModel = registrationViewModel,
            state = registrationViewModel.email,
            height = Padding.heightOutlineRegistrationsScreen,
            wight = Padding.widthOutlineRegistrationScreen,
            label = R.string.Email,
            paddingStart = Padding.paddingNormalTen,
            paddingTop = Padding.tvelv,
            paddingEnd = Padding.paddingNormalTen,
            paddingBottom = Padding.paddingNormalTen,
            background = BlueBlack,
            keyboardType = KeyboardType.Password,
            labelColor = registrationViewModel.textColorLabelEmail,
            clueText = R.string.EmailClue,
            clueColor = Grey,
            picker = {},
            onDoneAction = {
                clueEmail
            }
        )
        componetsRegistrations.TextField(
            registrationViewModel = registrationViewModel,
            state = registrationViewModel.number,
            height = Padding.heightOutlineRegistrationsScreen,
            wight = Padding.widthOutlineRegistrationScreen,
            label = R.string.Number,
            paddingStart = Padding.paddingNormalTen,
            paddingTop = Padding.tvelv,
            paddingEnd = Padding.paddingNormalTen,
            paddingBottom = Padding.paddingNormalTen,
            background = BlueBlack,
            keyboardType = KeyboardType.Phone,
            labelColor = registrationViewModel.textColorLabelNumber,
            clueText = R.string.NumberClue,
            clueColor = Grey,
            picker = {},
            onDoneAction = {
                clueNumber
            })
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            componetsRegistrations.TextField(
                registrationViewModel = registrationViewModel,
                state = registrationViewModel.birthday,
                height = Padding.heightOutlineRegistrationsScreen,
                wight = Padding.widthButtonLoginScreen,
                label = R.string.DateBirthday,
                paddingStart = Padding.paddingNormalTen,
                paddingTop = Padding.tvelv,
                paddingEnd = Padding.paddingNormalTen,
                paddingBottom = Padding.paddingNormalTen,
                background = BlueBlack,
                keyboardType = KeyboardType.Phone,
                labelColor = registrationViewModel.textLabelColorDate,
                clueText = R.string.DateBirthdayClue,
                clueColor = Grey,
                picker = {},
                onDoneAction = {
                    clueDate

                })
            componetsRegistrations.DataPicker(
                registrationViewModel = registrationViewModel,
                size = 30.dp
            )
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            componetsRegistrations.TextRecognizedOrCamera(
                registrationViewModel = registrationViewModel,
                state = registrationViewModel.textOrRecognezedId,
                wight = Padding.widthButtonLoginScreen,
                height = Padding.heightOutlineRegistrationsScreen,
                label = R.string.IdCard,
                paddingStart = Padding.paddingNormalTen,
                paddingTop = Padding.tvelv,
                paddingEnd = Padding.paddingNormalTen,
                paddingBottom = Padding.paddingNormalTen,
                contextToast = context,
                background = BlueBlack,
                labelColor = registrationViewModel.textLabelColorIDCard,
                clueText = R.string.IDCardClue,
                clueColor = Grey,
                iconBittonSize = 30.dp,
                icon = ImageVector.vectorResource(R.drawable.camera_alt_24),
                iconColor = colorOlivical,
                staTextOrReognezed = registrationViewModel.textOrRecognezedId,
                stateImage = registrationViewModel.imageBitmapTextRecognId,
                stateOrPermissions = registrationViewModel.isCameraPermission,
                regex = "\\bID\\d+\\b",
                actions = {
                    clueIdCard
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            componetsRegistrations.TextRecognizedOrCamera(
                registrationViewModel = registrationViewModel,
                state = registrationViewModel.textOrRecognezedPin,
                wight = Padding.widthButtonLoginScreen,
                height = Padding.heightOutlineRegistrationsScreen,
                label = R.string.PinCard,
                paddingStart = Padding.paddingNormalTen,
                paddingTop = Padding.tvelv,
                paddingEnd = Padding.paddingNormalTen,
                paddingBottom = Padding.paddingNormalTen,
                contextToast = context,
                background = BlueBlack,
                labelColor = registrationViewModel.textLabelColorPinCard,
                clueText = R.string.PinCardClue,
                clueColor = Grey,
                iconBittonSize = 30.dp,
                icon = ImageVector.vectorResource(R.drawable.camera_alt_24),
                iconColor = colorOlivical,
                staTextOrReognezed = registrationViewModel.textOrRecognezedPin,
                stateImage = registrationViewModel._imageBitmapTextRecognPin,
                stateOrPermissions = registrationViewModel.isCameraPermission,
                regex = """^\d{14}$""",
                actions = {
                    cluePinCard
                }
            )
        }

        if (clueLogin && cluePassword && clueDate && clueEmail &&
            clueName && clueSurname && clueNumber && clueIdCard && cluePinCard
        ) {
            registrationViewModel.isButtonEnabled.value = true
            registrationViewModel.colorEnabledValueButtonRegistrations.value
        }

        Button(
            onClick = {
                onClickNavigate()
                val auth = FirebaseAuth.getInstance()
                try {
                    if(userType.userType.value==true) {
                        registrationViewModel.userRegistrations(
                            auth = auth,
                            firestore = firestore,
                            storage = storage,
                            context = context,
                            name = registrationViewModel.festName.value,
                            surname = registrationViewModel.lastName.value,
                            email = registrationViewModel.email.value,
                            password = registrationViewModel.password.value,
                            imageUri = registrationViewModel.imageUriState.value,
                            login = registrationViewModel.login.value,
                            documentName = NameCollactionFirestore.UsersBlind,
                            pinCard = registrationViewModel.textOrRecognezedPin.value,
                            idCard = registrationViewModel.textOrRecognezedId.value,
                            registrationViewModel = registrationViewModel,
                            birdhday = registrationViewModel.birthday.value,
                            phone = registrationViewModel.number.value
                        )
                        registrationViewModel.isButtonEnabled.value = true
                    }else{
                        registrationViewModel.userRegistrations(
                            auth = auth,
                            firestore = firestore,
                            storage = storage,
                            context = context,
                            name = registrationViewModel.festName.value,
                            surname = registrationViewModel.lastName.value,
                            email = registrationViewModel.email.value,
                            password = registrationViewModel.password.value,
                            imageUri = registrationViewModel.imageUriState.value,
                            login = registrationViewModel.login.value,
                            documentName = NameCollactionFirestore.UsersVolonters,
                            pinCard = registrationViewModel.textOrRecognezedPin.value,
                            idCard = registrationViewModel.textOrRecognezedId.value,
                            registrationViewModel = registrationViewModel,
                            birdhday = registrationViewModel.birthday.value,
                            phone = registrationViewModel.number.value
                        )
                        registrationViewModel.isButtonEnabled.value = true
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Ошибка ${e.message}")
                }

            }, modifier = Modifier
                .width
                    (Padding.widthButtonLoginScreen)
                .height(Padding.heightButtonRegistrationScreen)
                .padding(
                    Padding.paddingNormalTen,
                    Padding.tvelv,
                    Padding.paddingNormalTen,
                    Padding.paddingSmall
                ),
            enabled = registrationViewModel.isButtonEnabled.value,
            colors = ButtonDefaults.buttonColors(registrationViewModel.colorEnabledValueButtonRegistrations.value)
        )
        {
            Text(
                text = "Вход",
                color = BlueBlack,
                fontSize = Padding.textSize,
                fontWeight = FontWeight.Bold
            )
        }


        // enabled = registrationViewModel.isButtonEnabled.value,)


        //  Toast.makeText(context,"Успешно ", Toast.LENGTH_SHORT).show()
        /* Row(
             modifier = Modifier.fillMaxSize(),
             horizontalArrangement = Arrangement.Center,
             verticalAlignment = Alignment.CenterVertically
         ) {
             componetsRegistrations.FaceDetectionsID(
                 iconBittonSize = 30.dp,
                 icon = ImageVector.vectorResource(R.drawable.camera_alt_24),
                 iconColor = colorOlivical,
                 stateOrPermissions = registrationViewModel.isCameraPermission,
                 contextToast = context,
                 stateImage = registrationViewModel.imageBitmapFaceRecogn,
                 stateFaceDetection = registrationViewModel.faceDetections,
                 text = registrationViewModel.errorTextFaceDetectionsId,
                 colorText = registrationViewModel.textColorDetectionsIdCard

             )
         }
         Row(
             modifier = Modifier.fillMaxSize(),
             horizontalArrangement = Arrangement.Center,
             verticalAlignment = Alignment.CenterVertically
         ){
             componetsRegistrations.FaceDetectionsPhoto(
                 iconBittonSize = 30.dp,
                 icon = ImageVector.vectorResource(R.drawable.camera_alt_24),
                 iconColor = colorOlivical,
                 stateOrPermissions = registrationViewModel.isCameraPermission,
                 contextToast = context,
                 stateImage = registrationViewModel.imageBitmapFaceRecogn,
                 stateFaceDetection = registrationViewModel.faceDetections,
                 text = registrationViewModel.errorTextFaceDetectionsPhoto,
                 colorText = registrationViewModel.textColorDetectionsIdCard

             )
         }*/
    }
}









