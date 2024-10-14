@file:Suppress("UNUSED_EXPRESSION")

package screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.diplom1.R
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.uiComponets.ComponetsLogin
import sence.kate.practica3.padding.Padding
import viewModel.LoginViewModel
import viewModel.TesseractViewModel

val componets = ComponetsLogin();
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onClickRegistrations: () -> Unit,
    onClickLogin: () -> Unit,
    tesseractViewModel: TesseractViewModel

    //nameNavagateHome:String
){
    //val navController = rememberNavController()
     val scope = rememberCoroutineScope()
 //   val context = LocalContext.current
     Column(
         modifier = Modifier
             .fillMaxSize()
             .clickable {
                 tesseractViewModel.speakText("Вы на экране регистрации, в середине экрана" +
                         "расположенны две кнопки для ввода Email и пароля, если вы еще не зарегистрированны," +
                         "нажмите на кнопку регистрации в конце экрана ")
             }
             .background(Color(BlueBlack.value)),

         verticalArrangement = Arrangement.Center,
         horizontalAlignment = Alignment.CenterHorizontally
     ) {
        componets.Image(
            image = R.drawable.wing,
            wight = 100.dp,
            height = 100.dp,
            paddingStart = Padding.paddingZero,
            paddingTop = 20.dp,
            paddingEnd = Padding.paddingZero
            )

         componets.OutlineTextField(
             viewModel = loginViewModel,
             state =  loginViewModel.login,
             wight = Padding.widthOutlineLoginScreen,
             height = Padding.heightOutlineLoginScreen,
             padding = Padding.paddingNormalTen,
             labelText = R.string.Email,
             colorOutline = loginViewModel.ColorOtline,
             tesseractViewModel = tesseractViewModel,
             text = "Email"

             )
         componets.OutlineTextField(
             viewModel =loginViewModel,
             state = loginViewModel.Password,
             wight = Padding.widthOutlineLoginScreen,
             height = Padding.heightOutlineLoginScreen,
             padding = Padding.paddingNormalTen,
             labelText = R.string.Password,
             colorOutline = loginViewModel.ColorOtline,
             tesseractViewModel = tesseractViewModel,
             text = "Пароль"
            )
         componets.Button(
             wight = Padding.widthButtonLoginScreen,
             height =Padding.heightButtonLoginScreen,
             buttonColor = colorOlivical,
             textSize = Padding.textLabelSize,
             textColor = BlueBlack,
             labelText = R.string.Vfod,
             onClick = {
                    onClickLogin()
                 tesseractViewModel.speakText("Вход")

                     },
             paddingStart = Padding.paddingSmall,
             paddingTop = Padding.paddingNormalTen,
             paddingEnd = Padding.paddingSmall,

         )
         componets.TextBold(
             viewModel = loginViewModel,
             textSize = Padding.textSize ,
             labelText = R.string.registrarions,
             paddingStart = Padding.tvelv,
             onClick ={ onClickRegistrations()},
             paddingTop =Padding.textTopRegistration ,
             paddingEnd = Padding.tvelv,
             text = "Еще не зарегистрированы? Регистрация",
             tesseractViewModel = tesseractViewModel
         )
     }
}

