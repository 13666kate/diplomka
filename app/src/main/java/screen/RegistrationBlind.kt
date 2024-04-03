package screen

import Logical.LogicalRegistrations.LogicalRegistrations
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.diplom1.R
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.Grey
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.uiComponets.ComponetsRegistrations
import sence.kate.practica3.padding.Padding
import viewModel.RegistrationViewModel

val componetsRegistrations = ComponetsRegistrations()
val registrationsClueText = LogicalRegistrations(
    registrationViewModel = RegistrationViewModel()
)

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
                picker ={},
                clueColor = Grey,
                onDoneAction = {
                    registrationsClueText.clueLastName(
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
                picker ={},
                clueColor = Grey,
                onDoneAction = {
                    registrationsClueText.clueLastName(
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

                    registrationsClueText.clueLogin(
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
                    registrationsClueText.cluePassword(
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
                    registrationsClueText.clueEmail(
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
                    registrationsClueText.clueNumber(
                        registrationViewModel.number,
                        registrationViewModel.textColorLabelNumber
                    )
                })
        Row(
            modifier = Modifier.fillMaxSize() ,
           horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            componetsRegistrations.TextField(
                registrationViewModel = registrationViewModel,
                state = registrationViewModel.birthday,
                height = Padding.heightOutlineRegistrationsScreen,
                wight = Padding. widthButtonLoginScreen,
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
                    registrationsClueText.clueDate(registrationViewModel.birthday,
                             registrationViewModel.textLabelColorDate )

                })
            componetsRegistrations.DataPicker(
                registrationViewModel = registrationViewModel,
                size = 30.dp)
        }
        }
    }






