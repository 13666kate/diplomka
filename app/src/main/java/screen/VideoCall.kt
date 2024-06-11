package screen

import DataClass.UserCardFriend
import WebRTS
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diplom1.R
import com.example.diplom1.ShedPreferences
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.Orange
import com.example.diplom1.ui.theme.Red
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.uiComponets.IDCardComponets
import firebase.FireBaseIDCardUser
import firebase.FirebaseRegistrations
import firebase.FirebaseString
import firebase.NameCollactionFirestore
import sence.kate.practica3.padding.Padding
import viewModel.CardVolonterViewModel
import viewModel.UserType
import viewModel.VideoCallViewModel

@Composable
fun AudioCallScreen(
    videoCallViewModel: VideoCallViewModel,
    context: Context,
    cardVolonterViewModel: CardVolonterViewModel,
    userType: UserType,
    navController: NavHostController,
    nameScreen: String
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBlack)
    ) {
        val userId = FirebaseRegistrations().userID()
        val list = remember { mutableStateListOf<UserCardFriend>() }
        val udFriends = remember { mutableStateOf("") }
        val listOme = cardVolonterViewModel.removeDuplicatesUserCardsFriends(list, cardVolonterViewModel.friendList)
        val startCall = remember { mutableStateOf(false) }
        val status = ShedPreferences.getUserType(context)
        val nameCollections = if (status == userType.UserVolonters.value) {
            NameCollactionFirestore.UsersBlind
        } else {
            NameCollactionFirestore.ReguestOrVolonter
        }

        LaunchedEffect(Unit) {
            FireBaseIDCardUser().idByEmailSearch(
                cardVolonterViewModel.emailStateCardUser.value,
                nameCollections = nameCollections,
                udFriends,
                nameFileInCollectionSearch = FirebaseString.email
            )
        }

        val idCardComponents = IDCardComponets()
        idCardComponents.LazyColumnList(
            context = context,
            cardVolonterViewModel = cardVolonterViewModel,
            userType = userType,
            onClick = {
                videoCallViewModel.idByEmailSearch(
                    stringSearch = cardVolonterViewModel.emailStateCardUser.value,
                    context = context,
                    userType = userType,
                    uidByStringSave = cardVolonterViewModel.saveID,
                    nameFileInCollectionSearch = FirebaseString.email
                )
                try {
                    //NavigationsMainActivity().webRTS.initiateCall()
                } catch (e: Exception) {
                    Log.e("Call", "Ошибка с инициализацией звонка:" + e.message.toString())
                }
                Log.e("id", "ID"+ WebRTS(cardVolonterViewModel).localUserId.toString())
                Log.e("id", "ID"+ WebRTS(cardVolonterViewModel).remoteUserId.toString())

                navController.navigate(nameScreen)
            }
        )
        Text(text = cardVolonterViewModel.saveID.value, modifier = Modifier.padding(16.dp))
    }
}
@Composable
fun Call(
    cardVolonterViewModel: CardVolonterViewModel,
    onClick: () -> Unit,
    answer: () -> Unit,
    reject: () -> Unit
) {
    val imageSize = 200.dp
 //   WebRTS().initiateCall()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBlack),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageBitmap = cardVolonterViewModel.imageStateCardUser.value?.asImageBitmap()
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = "imageStore",
                modifier = Modifier
                    .padding(top = 100.dp)
                    .size(imageSize)
                    .clip(CircleShape)
                    .border(
                        width = Padding.border,
                        brush = Brush.linearGradient(colors = listOf(colorOlivical, Orange)),
                        shape = CircleShape
                    ),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = cardVolonterViewModel.imageAvatarValueNo.value),
                contentDescription = "imageStore",
                modifier = Modifier
                    .padding(top = 100.dp)
                    .size(imageSize)
                    .border(
                        width = Padding.border,
                        brush = Brush.linearGradient(colors = listOf(colorOlivical, Orange)),
                        shape = CircleShape
                    ),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(colorOlivical)
            )
        }

        Text(
            text = "${cardVolonterViewModel.nameStateCardUser.value} ${cardVolonterViewModel.surnameStateCardUser.value}",
            modifier = Modifier.padding(top = 80.dp),
            style = TextStyle(
                color = colorOlivical,
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.registration, FontWeight.Bold)),
                textAlign = TextAlign.Center
            )
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            val iconSize = 100.dp
            IconButton(
                modifier = Modifier
                    .size(iconSize)
                    .border(
                        width = Padding.border,
                        brush = Brush.linearGradient(colors = listOf(Red, Red)),
                        shape = CircleShape
                    ),
                onClick = onClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.phone_call),
                    contentDescription = "Add image",
                    modifier = Modifier
                        .size(iconSize)
                        .padding(10.dp),
                    tint = Red
                )
            }
            Column {
                Button(onClick = answer) {
                    Text("Позвонить")
                }
                //Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = reject) {
                    Text("Принять звонок")
                }
            }
        }
    }
}
    @Composable
    fun CallScreen(
        navController: NavHostController,
        cardVolonterViewModel: CardVolonterViewModel,
        webRTS: WebRTS,
        context: Context
    ) {
        // Ensure webRTS is initialized
        LaunchedEffect(Unit) {
            webRTS.initializeWebRTC(context)
        }

        Call(
            cardVolonterViewModel = cardVolonterViewModel,
            onClick = {
                navController.navigate("Click") {
                    popUpTo("Click") { inclusive = true }
                }
            },
            answer = {
                try {
                    webRTS.answerCall()
                } catch (e: Exception) {
                    Log.e("Call", e.message.toString())
                }
            },
            reject = {
                try {
                    webRTS.initiateCall()
                } catch (e: Exception) {
                    Log.e("Call", e.message.toString())
                }
            }
        )
    }





/*@Preview
@Composable

 fun MyScreen(){
     val contexxt = LocalContext.current
    Call(CardVolonterViewModel(), onClick = {

    })
 }*/



/*@Preview
@Composable

 fun MyScreen(){
     val contexxt = LocalContext.current
    VideoCallScreen(
       cardVolonterViewModel =  CardVolonterViewModel(),
        userType = UserType(),
        context =contexxt
    )
 }*/
   /* Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BlueBlack),
        verticalArrangement = Arrangement.Center
    ) {
        val uyd = FirebaseRegistrations().userID()
        val list = remember {
            mutableStateListOf<UserCardFriend>()
        }
        val udFriends = remember {
            mutableStateOf("")
        }
        list.addAll(cardVolonterViewModel.friendList)

        val roomName = remember { mutableStateOf("") }
        val startCall = remember { mutableStateOf(false) }
        val status = ShedPreferences.getUserType(context)
        val nameCollections = if (status == userType.UserVolonters.value) {
            NameCollactionFirestore.UsersBlind
        } else {
            NameCollactionFirestore.ReguestOrVolonter
        }
        LaunchedEffect(Unit) {
            FireBaseIDCardUser().idByEmailSearch(
                cardVolonterViewModel.emailStateCardUser.value,
                nameCollections = nameCollections,
                udFriends,
                nameFileInCollectionSearch = FirebaseString.email

            )
        }
   *//*     videoCallViewModel.geyUidFriens(
            cardVolonterViewModel= cardVolonterViewModel,
            userType = userType,
            context = context,
            email = userdata.email,
            udFriends = udFriends)*//*
        // LazyColumn for displaying user list
        LazyColumn(
            modifier = Modifier
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            itemsIndexed(list) { _, userdata ->
                Box(modifier = Modifier) {
                    RowTwo(
                        baground = colorOlivical,
                        imageSize = 75.dp,
                        name = userdata.nane,
                        surname = userdata.surname,
                        bitmap = userdata.bitmap,
                        adress = userdata.adress,
                        number = userdata.number,
                        region = userdata.region,
                        rayon = userdata.rayon,
                        birthday = userdata.birhday,
                        yersVolonters = userdata.yersVolonters,
                        aboutMe = userdata.anoutMe,
                        textSize = 18.sp,
                        textColor = BlueBlack,
                        email = userdata.email,
                        sizeEmail = 18.sp,
                        stateBitmap = cardVolonterViewModel.imageStateCardUser,
                        stateName = cardVolonterViewModel.nameStateCardUser,
                        stateSurname = cardVolonterViewModel.surnameStateCardUser,
                        stateEmail = cardVolonterViewModel.emailStateCardUser,
                        stateAboutMe = cardVolonterViewModel.aboutmeUserCarsd,
                        stateNumber = cardVolonterViewModel.numberUserCarsd,
                        stateAddress = cardVolonterViewModel.adresStateCardUser,
                        stateBirthday = cardVolonterViewModel.birhdayUserCards,
                        stateRayon = cardVolonterViewModel.rayonStateCardUser,
                        stateRegion = cardVolonterViewModel.regionStateCardUser,
                        stateVoloYesrs = cardVolonterViewModel.expVolonters,
                        onClick = {
                            val email = userdata.email
                            if (email.isNotEmpty()) {
                                // Launch a coroutine in the Composable scope

                                videoCallViewModel.idByEmailSearch(
                                    userType = userType,
                                    nameFileInCollectionSearch = FirebaseString.email,
                                    stringSearch = userdata.email,
                                    context = context,
                                    uidByStringSave = udFriends,
                                    onResult = { success ->
                                        if (success) {
                                            val room = UUID.randomUUID().toString()
                                            videoCallViewModel.createCall(
                                                room,
                                                uyd.toString(),
                                                udFriends.value
                                            )
                                            roomName.value = room
                                            startCall.value = true
                                            Log.e("call", "Call started with room: $room")
                                        } else {
                                            Log.e("call", "Friend UID not found")
                                        }
                                        Toast.makeText(
                                            context,
                                            videoCallViewModel.uidFriends.value,
                                            Toast.LENGTH_SHORT
                                        ).show()


                                        if (udFriends.value.isNotEmpty()) {
                                            val room = UUID.randomUUID().toString()
                                            videoCallViewModel.createCall(
                                                room,
                                                uyd.toString(),
                                                videoCallViewModel.uidFriends.value
                                            )

                                            roomName.value = room
                                            startCall.value = true
                                        }

                                    })
                            }
                        })
                }
            }
        }

        // Start video call when the room name is set
        if (startCall.value && roomName.value.isNotEmpty()) {
            StartJitsiCall(roomName.value)
        }

        // Listen for incoming calls
        videoCallViewModel.listenForCalls(uyd.toString()) { room ->
            roomName.value = room
            startCall.value = true
        }
    }
}

@Composable
fun StartJitsiCall(roomName: String) {
    val options = JitsiMeetConferenceOptions.Builder()
        .setRoom(roomName)
        .build()
    JitsiMeetActivity.launch(LocalContext.current, options)
}


        *//*Button(onClick = {
           videoCallViewModel.createCall("room",uyd.toString(),uyd.toString())
           startVideoCall(context) }
        ) {
            Text("Start Call")
        }
    }*//*

        fun startVideoCall(context: Context) {
            val options = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("https://meet.jit.si"))
                .setRoom("room")
                .build()

            JitsiMeetActivity.launch(context, options)
        }

*/


/*
fun generateTokenWithRhino(appId: String, appCertificate: String, channelName: String, uid: Int, expirationTimeInSeconds: Int): String {
    val script = """
        const Agora = require('agora-access-token');
        const appID = '$appId';
        const appCertificate = '$appCertificate';
        const channelName = '$channelName';
        const uid = $uid;
        const role = Agora.RtcRole.PUBLISHER;
        const expirationTimeInSeconds = $expirationTimeInSeconds;
        const currentTimestamp = Math.floor(Date.now() / 1000);
        const privilegeExpiredTs = currentTimestamp + expirationTimeInSeconds;
        const token = Agora.RtcTokenBuilder.buildTokenWithUid(appID, appCertificate, channelName, uid, role, privilegeExpiredTs);
        token;
    """.trimIndent()

    val ctx: Context = Context.enter()
    return try {
        ctx.languageVersion = Context.VERSION_ES6
        val scope: Scriptable = ctx.initStandardObjects()
        ctx.evaluateString(scope, script, "script", 1, null).toString()
    } finally {
        Context.exit()
    }
}*/
/*
fun generateToken(
    appId: String,
    appCertificate: String,
    channelName: String,
    uid: Int,
    expirationTimeInSeconds: Int
): String {
    val tokenBuilder = RtcTokenBuilder2()
    val timestamp = (System.currentTimeMillis() / 1000 + expirationTimeInSeconds).toInt()
    return tokenBuilder.buildTokenWithUid(
        appId,
        appCertificate,
        channelName,
        uid,
        RtcTokenBuilder2.Role.ROLE_PUBLISHER,
        timestamp,
        timestamp
    )
}*/
