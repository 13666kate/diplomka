@file:Suppress("UNUSED_EXPRESSION")
package screen

import androidx.camera.core.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.diplom1.R
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.colorOlivical
import com.google.firebase.annotations.PreviewApi
import sence.kate.practica3.padding.Padding


@Composable
fun UserType(
            onclickButtonTypeBlind:()->Unit,
            onclickButtonTypeVolonter:()->Unit,
             ) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBlack),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

   Image(painter = painterResource(
       id = R.drawable.lololo),
       contentDescription ="add",
       modifier = Modifier
           .size(290.dp))
        Button(onClick = { onclickButtonTypeBlind() },
            modifier = Modifier
                .width(Padding.widthButtonLoginScreen,)
                .height(Padding.heightButtonLoginScreen)
                .padding(Padding.paddingNormalTen,Padding.tvelv,Padding.paddingNormalTen),
            colors = ButtonDefaults.buttonColors(colorOlivical)) {
          Text(text = stringResource(R.string.userBlind ),
              style = TextStyle(
                  color = BlueBlack,
                  fontSize = Padding.textSize,
                  fontWeight = FontWeight.Bold,
              )
          )

        }
        Button(onClick = { onclickButtonTypeVolonter() },
            modifier = Modifier
                .width(Padding.widthButtonLoginScreen,)
                .height(Padding.heightButtonLoginScreen)
                .padding(Padding.paddingNormalTen,Padding.tvelv,Padding.paddingNormalTen),
            colors = ButtonDefaults.buttonColors(colorOlivical)) {
            Text(text = stringResource(R.string.userVolonter),
                style = TextStyle(
                    color = BlueBlack,
                    fontSize = Padding.textSize,
                    fontWeight = FontWeight.Bold,
                ))
        }
      

    }
}



