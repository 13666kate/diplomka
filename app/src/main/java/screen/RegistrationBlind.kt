package screen

import Logical.LogicalRegistrations.LogicalRegistrations
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
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
import sence.kate.practica3.padding.Padding
import viewModel.RegistrationViewModel

val componetsRegistrations = ComponetsRegistrations()
val auth = FirebaseAuth.getInstance()
val firestore = FirebaseFirestore.getInstance()
val storage = FirebaseStorage.getInstance()

@Composable
fun RegistrationBlind(
    registrationViewModel: RegistrationViewModel,
    onClick: () -> Unit,
    context: Context,
) {

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
                colorImageTrue = colorOlivical
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
                    registrationViewModel.clueLastName(
                        registrationViewModel.festName,
                        registrationViewModel.textColorLabelFestName
                    )
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
                    registrationViewModel.clueLastName(
                        registrationViewModel.lastName,
                        registrationViewModel.textColorLabelLastName
                    )
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

                    registrationViewModel.clueLogin(
                        textEmail = registrationViewModel.login,
                        textColor = registrationViewModel.textColorLogin
                    )
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
                registrationViewModel.cluePassword(
                    registrationViewModel.password,
                    registrationViewModel.textColorPassword
                )
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
                registrationViewModel.clueEmail(
                    registrationViewModel.email,
                    registrationViewModel.textColorLabelEmail
                )
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
                registrationViewModel.clueNumber(
                    registrationViewModel.number,
                    registrationViewModel.textColorLabelNumber
                )
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
                    registrationViewModel.clueDate(
                        registrationViewModel.birthday,
                        registrationViewModel.textLabelColorDate
                    )

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
                    registrationViewModel.clueIdCard(
                        state = registrationViewModel.textOrRecognezedId,
                        textColorIdCard = registrationViewModel.textLabelColorIDCard
                    )
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
                    registrationViewModel.cluePinCard()
                }
            )
        }
        componetsRegistrations.Button(

            onClick = {
                val auth = FirebaseAuth.getInstance()

                try {


                          registrationViewModel.isButtonEnabled.value = true
                        LogicalRegistrations().registerUser(
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
                            documentName = "usersBlind"

                        )


                } catch (e: Exception) {
                    Log.e(TAG, "Ошибка ${e.message}")
                }
            },
            wight = Padding.widthButtonLoginScreen,
            height = Padding.heightButtonLoginScreen,
            buttonColor = colorOlivical,
            textSize = Padding.textSize,
            textColor = Color.DarkGray,
            labelText = R.string.registrarions,
            paddingTop = Padding.tvelv,
            paddingEnd = Padding.paddingNormalTen,
            paddingStart = Padding.paddingNormalTen,
            enabled =  registrationViewModel.isButtonEnabled.value
        )


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








