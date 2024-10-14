@file:Suppress("UNUSED_EXPRESSION")
package screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.example.diplom1.R
import com.example.diplom1.ShedPreferences
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.colorOlivical
import sence.kate.practica3.padding.Padding
import viewModel.TesseractViewModel
import viewModel.UserType

 val tesseractViewModelO = TesseractViewModel()
@Composable
fun UserType(
            onclickButtonTypeBlind:()->Unit,
            onclickButtonTypeVolonter:()->Unit,
            context:Context,
            userType: UserType,
            tesseractViewModel:TesseractViewModel,
            lifecycleOwner: LifecycleOwner
             ) {
 //   val lifecycleOwner = LocalLifecycleOwner.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBlack)
            .clickable {
                tesseractViewModel.speakText("Здравствуйте, вас приведствует приложение Sighted peple В середине" +
                        "экрана расположены две кнопки, выберите тип пользователя")
            },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

   Image(painter = painterResource(
       id = R.drawable.lololo),
       contentDescription ="add",
       modifier = Modifier
           .size(290.dp))
        Button(onClick = {

            onclickButtonTypeBlind()
            val status= ShedPreferences.getUserTypeStatus(context)
          userType.userTypeTrue()
            Toast.makeText(context,status.toString(), Toast.LENGTH_SHORT).show()
                         },
            modifier = Modifier
                .width(Padding.widthButtonLoginScreen,)
                .height(Padding.heightButtonLoginScreen)
                .padding(Padding.paddingNormalTen,Padding.tvelv,Padding.paddingNormalTen),
            colors = ButtonDefaults.buttonColors(colorOlivical)) {
          Text(text = stringResource(R.string.userBlind ),
              modifier = Modifier.clickable {
                  tesseractViewModel.speakText("Пользователь")
              },
              style = TextStyle(
                  color = BlueBlack,
                  fontSize = Padding.textSize,
                  fontWeight = FontWeight.Bold,
              )
          )

        }
        Button(onClick = {
            tesseractViewModel.speakText("Волонтер")
          userType.userTypeFalse()
            onclickButtonTypeVolonter()
            ShedPreferences.saveUserTypeStatus(context,ShedPreferences.userFalse)
        },
            modifier = Modifier
                .width(Padding.widthButtonLoginScreen,)
                .height(Padding.heightButtonLoginScreen)
                .padding(Padding.paddingNormalTen,Padding.tvelv,Padding.paddingNormalTen),
            colors = ButtonDefaults.buttonColors(colorOlivical)) {
            Text(text = stringResource(R.string.userVolonter),
                modifier = Modifier.clickable {
                    tesseractViewModel.speakText("Волонтер")
                },
                style = TextStyle(
                    color = BlueBlack,
                    fontSize = Padding.textSize,
                    fontWeight = FontWeight.Bold,
                ))
        }
      

    }
}



