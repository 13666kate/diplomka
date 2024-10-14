package screen

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.example.diplom1.R
import com.example.diplom1.ShedPreferences
import com.example.diplom1.ui.theme.Black
import com.example.diplom1.ui.theme.BlackWhite
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.Red
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.uiComponets.ComponetsHome
import com.example.diplom1.uiComponets.ComponetsRegistrations
import sence.kate.practica3.padding.Padding
import viewModel.CardVolonterViewModel
import viewModel.HomeScreenViewModel
import viewModel.ProfileViewModel
import viewModel.TesseractViewModel
import viewModel.UserType
import viewModel.VideoCallViewModel

val componetsHome = ComponetsHome()

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenUserBlind(
    homeViewModel: HomeScreenViewModel,
    onClickCall: () -> Unit,
    cardVolonterViewModel: CardVolonterViewModel,
    tesseractViewModel: TesseractViewModel,
    videoCallViewModel: VideoCallViewModel,
    context: Context,
    userType: UserType,
    onClickTextRecognized: () -> Unit,
    onClickNotification: () -> Unit,
    onClickChat: () -> Unit,
    onClickVideo: () -> Unit,
    onClickSos: () -> Unit,
    lifecycleOwner: LifecycleOwner,
    profileViewModel:ProfileViewModel,
) {
    val incomingCall by videoCallViewModel.incomingCall.observeAsState()
    tesseractViewModel.setTextColorListen(context)
    tesseractViewModel.setTextColorBrayl(context)
    LaunchedEffect(lifecycleOwner) {
        tesseractViewModel.initializeTTS(context)
        tesseractViewModel.handleLifecycle(context, lifecycleOwner)
    }
    profileViewModel.getData(
        userType, context, cardVolonterViewModel
    )
val status = ShedPreferences.getUserType(context)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = BlueBlack)
                .padding(top = 7.dp, start = 13.dp)
                .clickable {
                    if (status == userType.UserBlind.value) {
                        tesseractViewModel.speakText(
                            "Вас приветствует главный экран" +
                                    "Вверху отображается ваше фото, имя , фамилия и Email" +
                                    "ниже располодены,слева-уведомления, посередине кнопка голосового соправождения," +
                                    "справа- клавиатура Брайля" +
                                    "Еще ниже расположен чат, видеочат, конвертер, звонок, Кнопка экстренной помощи "
                        )
                    }
                }
        ) {
            //androidx.compose.material.Text(text = TesseractViewModel().text.value, color = colorOlivical)





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
                            text = profileViewModel.nameUser.value,
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
                            text = profileViewModel.surnameUser.value,
                            fontSize = Padding.textSizeHomeBorderName,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    componetsHome.Text(
                        paddingBottom = 20.dp,
                        paddingTop = 0.dp,
                        paddingStart = 0.dp,
                        color = colorOlivical,
                        text = profileViewModel.email.value,
                        fontSize = Padding.textSizeHomeBorderEmail,
                        fontWeight = FontWeight.Bold
                    )

                }

            }
            val iconButtonSize = 50.dp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .background(color = colorOlivical, shape = RoundedCornerShape(25.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(7.dp)
                        .background(color = colorOlivical, shape = RoundedCornerShape(25.dp)),
                    horizontalArrangement = Arrangement.SpaceBetween, // This arranges the children with equal spacing
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.Center

                        //   .padding(end = 50.dp)
                    ) {
                        componetsRegistrations.IconButton(
                            size = iconButtonSize, icon = Icons.Default.Notifications,
                            iconColor = BlueBlack
                        ) {
                            if (componetsRegistrations.stusTTs(context = context)){
                                tesseractViewModel.speakText("уведомления")
                            }
                            onClickNotification()
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 20.dp, end = 20.dp)
                            .border(
                                width = Padding.border,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        tesseractViewModel.iconColorClick.value,
                                        tesseractViewModel.iconColorClick.value
                                    )
                                ), shape = CircleShape
                            ),
                    ) {
                        componetsRegistrations.IconButtonImage(
                            size = iconButtonSize,
                            icon = painterResource(id = R.drawable.lisstenone),
                            iconColor = BlueBlack
                        ) {
                            tesseractViewModel.saveSateteButtonListen(context)
                            val text = if (componetsRegistrations.stusTTs(context = context)==false) {
                                "голосовое соправождение активировано "
                            } else {
                                "голосовое соправождение отключено  "
                            }
                            tesseractViewModel.speakText(text)


                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 20.dp, end = 20.dp)
                            .border(
                                width = Padding.border,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        tesseractViewModel.iconBraylColorClick.value,
                                        tesseractViewModel.iconBraylColorClick.value
                                    )
                                ), shape = CircleShape
                            ),
                    ) {
                        componetsRegistrations.IconButtonImage(
                            size = iconButtonSize, icon = painterResource(id = R.drawable.brayl),
                            iconColor = BlueBlack
                        ) {
                            tesseractViewModel.saveSateteButtonBrayl(context)
                            val status = ShedPreferences.getShedPreferences(
                                context = context,
                                UserFileCollections = ShedPreferences.CollectionsBrayl,
                                keyFile = ShedPreferences.braylKey
                            )

                            if (componetsRegistrations.stusTTs(context = context)) {
                                val text = if (status == ShedPreferences.yes) {
                                    "клавиатура активирована"
                                } else {
                                    "клавиатура отключена "
                                }
                                tesseractViewModel.speakText(text)

                            }



                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 135.dp)
                    .verticalScroll(rememberScrollState())
            ) {
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
                            onClickCall = {
                                if (ComponetsRegistrations().stusTTs(context = context)) {
                                    tesseractViewModel.speakText( "чат")
                                }

                                onClickChat()
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
                            text = "видеочат ",
                            textColor = BlackWhite,
                            bagroundAliment = colorOlivical,
                            phonAliment = BlackWhite,
                            onClickCall ={
                                if (componetsRegistrations.stusTTs(context = context)) {
                                    tesseractViewModel.speakText("Видеочат")
                                }
                                onClickVideo()

                            })
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
                            onClickCall = {
                                if (componetsRegistrations.stusTTs(context = context)) {
                                    tesseractViewModel.speakText("Звонок")
                                }
                                onClickCall() }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    )
                    {
                        val isPlaying = remember {
                            derivedStateOf {
                                homeViewModel.isPlaying.value
                            }
                        }
                        LaunchedEffect(Unit) {
                            homeViewModel.initMediaPlayer(
                                lifecycleOwner = lifecycleOwner,
                                context = context
                            )
                        }
                        componetsHome.AlimentClickedHome(
                            painter = R.drawable.gromofon,
                            colorBottomText = Gray,
                            text = "SOS",
                            textColor = BlackWhite,
                            bagroundAliment = Red,
                            phonAliment = BlackWhite,
                            onClickCall = {
                                homeViewModel.start()
                                homeViewModel.requestSos.value = true
                                if (componetsRegistrations.stusTTs(context = context)) {
                                    tesseractViewModel.speakText("sos")
                                }
                                if (isPlaying.value) {
                                    homeViewModel.stop()
                                    homeViewModel.requestSos.value = false
                                } else {
                                    homeViewModel.start()

                                }
                                homeViewModel.requestSos()

                                onClickSos()
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
                                onClickCall = {
                                    if (componetsRegistrations.stusTTs(context = context)) {
                                        tesseractViewModel.speakText("конвертер")
                                    }
                                    onClickTextRecognized() }
                            )
                        }
                     /*   Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp)
                        )
                        {
                            val isPlaying = remember {
                                derivedStateOf {
                                    homeViewModel.isPlaying.value
                                }
                            }
                            LaunchedEffect(Unit) {
                                homeViewModel.initMediaPlayer(
                                    lifecycleOwner = lifecycleOwner,
                                    context = context
                                )
                            }
                            componetsHome.AlimentClickedHome(
                                painter = R.drawable.gromofon,
                                colorBottomText = androidx.compose.ui.graphics.Color.Companion.Gray,
                                text = "SOS",
                                textColor = BlackWhite,
                                bagroundAliment = Red,
                                phonAliment = BlackWhite,
                                onClickCall = {
                                    homeViewModel.start()
                                    homeViewModel.requestSos.value = true
                                    if (componetsRegistrations.stusTTs(context = context)) {
                                        tesseractViewModel.speakText("sos")
                                    }
                                    if (isPlaying.value) {
                                        homeViewModel.stop()
                                        homeViewModel.requestSos.value = false
                                    } else {
                                        homeViewModel.start()

                                    }
                                    homeViewModel.requestSos()

                                    onClickSos()
                                }
                            )

                        }*/
                    }
                }

                /*       OutlinedTextField(
               messageText.value,
                onValueChange = { newLogin ->
                    messageText.value = newLogin
                },
                singleLine = true,
                textStyle = TextStyle(colorOlivical, fontSize = Padding.textSize),

                modifier = Modifier
                    .padding(10.dp)
                    .width(200.dp)
                    .height(50.dp),
                label = {
                    Text(
                        text ="Сообщение",
                        color = colorOlivical,
                        style = TextStyle(
                            fontSize = Padding.textLabelSize,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Email
                ),

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = chatViewModel.textLabelColor.value,
                    focusedBorderColor = chatViewModel.textLabelColorClick.value,
                    focusedLabelColor = chatViewModel.textLabelColorClick.value,
                    cursorColor = chatViewModel.textLabelColorClick.value,


                    ),

                shape = RoundedCornerShape(Padding.shape),
            )*/
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

        val ststusTTs =
            ShedPreferences.getShedPreferences(
                context = context,
                UserFileCollections = ShedPreferences.CollectionsTTs,
                keyFile = ShedPreferences.buttonState
            )
        val ststusTTsz =
            ShedPreferences.getShedPreferences(
                context = context,
                UserFileCollections = ShedPreferences.CollectionsTTs,
                keyFile = ShedPreferences.keyTts
            )

        Log.d("setTextColor", "Initial Status button: $ststusTTs}")
        Log.d("setTextColor", "Initial Status TTs: $ststusTTsz}")


}

/*

@Preview
@Composable
fun Screen() {
    HomeScreenUserBlind(HomeScreenViewModel(), onClickCall = {}, CardVolonterViewModel())
}


*/



