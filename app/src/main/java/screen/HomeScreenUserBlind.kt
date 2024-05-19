package screen


import android.app.Notification
import android.content.Context
import android.graphics.Bitmap


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text


import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import com.example.diplom1.ui.theme.BlueBlack
import firebase.FirebaseRegistrations
import viewModel.HomeScreenViewModel

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diplom1.R
import com.example.diplom1.ShedPreferences
import com.example.diplom1.ui.theme.Black
import com.example.diplom1.ui.theme.BlackWhite
import com.example.diplom1.ui.theme.Orange
import com.example.diplom1.ui.theme.Red
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.uiComponets.ComponetsHome
import firebase.NameCollactionFirestore
import sence.kate.practica3.padding.Padding
import viewModel.CardVolonterViewModel
import viewModel.UserType
import java.nio.file.WatchEvent

val componetsHome = ComponetsHome()

@Composable
fun HomeScreenUserBlind(
    homeViewModel: HomeScreenViewModel,
    onClickCall: () -> Unit,
    cardVolonterViewModel: CardVolonterViewModel,
    context: Context,
    userType: UserType,
    onClickNotification: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BlueBlack)
            .padding(top = 7.dp, start = 13.dp)

    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(
                    // color = BlueBlackDark,
                    shape = RoundedCornerShape(70.dp),
                    brush = Brush.linearGradient(
                        colors = listOf(colorOlivical, BlackWhite, Black)
                    ),
                    // alpha = 3.0f
                )
                .padding(top = 7.dp)


        ) {

            Box(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                componetsHome.ImageVar(
                    viewModel = homeViewModel,
                    size = 110.dp
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(start = 2.dp)
                    .width(100.dp),
                horizontalAlignment = Alignment.Start

            )
            {
                Row(
                ) {
                    componetsHome.Text(
                        paddingBottom = 30.dp,
                        color = colorOlivical,
                        text = "name",
                        paddingStart = 0.dp,
                        paddingTop = 30.dp,
                        fontSize = Padding.textSizeHomeBorderName,
                        fontWeight = FontWeight.Bold
                    )
                    componetsHome.Text(
                        paddingBottom = 30.dp,
                        paddingTop = 30.dp,
                        paddingStart = 12.dp,
                        color = colorOlivical,
                        text = "surname",
                        fontSize = Padding.textSizeHomeBorderName,
                        fontWeight = FontWeight.Bold
                    )
                }
                componetsHome.Text(
                    paddingBottom = 20.dp,
                    paddingTop = 0.dp,
                    paddingStart = 0.dp,
                    color = colorOlivical,
                    text = "my.okean.of.dreams@gmail.com",
                    fontSize = Padding.textSizeHomeBorderEmail,
                    fontWeight = FontWeight.Bold
                )

            }

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .background(color = colorOlivical, shape = RoundedCornerShape(25.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .padding(start = 10.dp)
                    .background(color = colorOlivical, shape = RoundedCornerShape(25.dp))
            ) {
                val iconButtinSize = 40.dp
                componetsRegistrations.IconButton(
                    size = iconButtinSize, icon = Icons.Default.Notifications,
                    iconColor = BlueBlack
                ) {
                 onClickNotification()
                }
            }
        }
        Column(modifier = Modifier
            .fillMaxSize().
                padding(bottom = 135.dp)
            .verticalScroll(rememberScrollState())) {
            Row(
                modifier = Modifier
                    .height(200.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(top = 30.dp)
                )
                {
                    componetsHome.AlimentClickedHome(
                        painter = R.drawable.baseline_chat_24,
                        colorBottomText = Gray,
                        text = "чат ",
                        textColor = BlackWhite,
                        bagroundAliment = colorOlivical,
                        phonAliment = BlackWhite,
                        {

                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)
                )
                {
                    componetsHome.AlimentClickedHome(
                        painter = R.drawable.baseline_video_chat_24,
                        colorBottomText = Gray,
                        text = "Видеочат ",
                        textColor = BlackWhite,
                        bagroundAliment = colorOlivical,
                        phonAliment = BlackWhite,
                        {}
                    )
                }
            }
            Row() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(top = 20.dp)
                )
                {
                    componetsHome.AlimentClickedHome(
                        painter = R.drawable.baseline_call_24,
                        colorBottomText = Gray,
                        text = "звонок",
                        textColor = BlackWhite,
                        bagroundAliment = colorOlivical,
                        phonAliment = BlackWhite,
                        onClickCall = onClickCall
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                )
                {

                    componetsHome.AlimentClickedHome(
                        painter = R.drawable.baseline_laptop_book_24,
                        colorBottomText = Gray,
                        text = "запись",
                        textColor = BlackWhite,
                        bagroundAliment = colorOlivical,
                        phonAliment = BlackWhite,
                        {

                        }
                    )
                }
            }
            val status = ShedPreferences.getUserType(context)
            if (status == userType.UserBlind.value) {

                Row() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(top = 30.dp)
                    )
                    {
                        componetsHome.AlimentClickedHome(
                            painter = R.drawable.camera_alt_24,
                            colorBottomText = Gray,
                            text = "конвертер",
                            textColor = BlackWhite,
                            bagroundAliment = colorOlivical,
                            phonAliment = BlackWhite,
                            {

                            }
                        )
                    }
                }
            }
            /* Row(
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(top = 50.dp)
             )
             {

                 componetsHome.AlimentClickedHome(
                     painter = R.drawable.baseline_laptop_book_24,
                     colorBottomText = Gray,
                     text = "запись",
                     textColor = BlackWhite,
                     bagroundAliment = colorOlivical,
                     phonAliment = BlackWhite
                 )
             }*/


        }
    }

    /*  Row(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding()
        ) {
            componetsHome.AlimentClickedHome(
                painter = R.drawable.baseline_chat_24,
                colorBottomText = Gray,
                text = "Чат ",
                textColor = BlackWhite,
                bagroundAliment = colorOlivical,
                phonAliment = BlackWhite
            )
        }

        componetsHome.AlimentClickedHome(
            painter = R.drawable.baseline_chat_24,
            colorBottomText = Gray,
            text = "Чат ",
            textColor = BlackWhite,
            bagroundAliment = colorOlivical,
            phonAliment = BlackWhite
        )
        componetsHome.AlimentClickedHome(
            painter = R.drawable.baseline_video_chat_24,
            colorBottomText = Gray,
            text = "Видеозвонок",
            textColor = BlackWhite,
            bagroundAliment = colorOlivical,
            phonAliment = BlackWhite
        )




    componetsHome.AlimentClickedHome(
        painter = R.drawable.baseline_call_24,
        colorBottomText = Gray,
        text = "Звонок",
        textColor = BlackWhite,
        bagroundAliment = colorOlivical,
        phonAliment = BlackWhite
    )

}
    componetsHome.AlimentClickedHome(
        painter = R.drawable.baseline_laptop_book_24,
        colorBottomText = Gray,
        text = "запись",
        textColor = BlackWhite,
        bagroundAliment = colorOlivical,
        phonAliment = BlackWhite
    )*/


}

/*

@Preview
@Composable
fun Screen() {
    HomeScreenUserBlind(HomeScreenViewModel(), onClickCall = {}, CardVolonterViewModel())
}


*/



