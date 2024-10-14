package screen


import DataClass.Message
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diplom1.ShedPreferences
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.Orange
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.uiComponets.IDCardComponets
import firebase.FireBaseIDCardUser
import firebase.FirebaseRegistrations
import firebase.FirebaseString
import viewModel.CardVolonterViewModel
import viewModel.ChatViewModel
import viewModel.TesseractViewModel
import viewModel.UserType
import viewModel.VideoCallViewModel

@Composable
fun ListFrendChat(
    context: Context,
    userType: UserType,
    chatViewModel: ChatViewModel = viewModel(),
    cardVolonterViewModel: CardVolonterViewModel,
    navigateToChat: () -> Unit,
    onClickBack:() ->Unit,
    videoCallViewModel: VideoCallViewModel,
) {
    val idCardsComponets = IDCardComponets()
    chatViewModel.auchID.value = FirebaseRegistrations().userID().toString()
    val friends = remember { mutableStateListOf<String>() } // Список друзей, нужно инициализировать

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBlack)
    ) {
        val iconSize = 60.dp
        val iconColor = colorOlivical
        val  status= ShedPreferences.getUserType(context)
        val idList = remember {
            mutableListOf<String>()
        }
        LaunchedEffect(Unit) {
            if (status == userType.UserVolonters.value) {


                val list = FireBaseIDCardUser().getFriendList(
                    context = context,
                    userType = userType,
                    cardVolonterViewModel = cardVolonterViewModel
                )
                Log.e("listil" , "${list}")
                for (intex in list) {
                    idList.addAll(cardVolonterViewModel.idByEmailSearchList(
                        context = context,
                        userType = userType,
                        nameFileInCollectionSearch = FirebaseString.email,
                        stringSearch = intex.email
                    ))
                    Log.e("listil", "list : " + "${idList}" )
                }
            }
        }
      /*  screen.Text(text = chatViewModel.saveID, informations ="save" )
        screen.Text(text = chatViewModel.auchID, informations ="ID" )*/
        Column(modifier = Modifier.padding(10.dp)) {
            componetsRegistrations.IconButton(
                size = iconSize,
                icon = Icons.Default.ArrowBack,
                iconColor = iconColor,
                onClick = onClickBack)

        }
        idCardsComponets.LazyColumnListO(
            context = context,
            cardVolonterViewModel = cardVolonterViewModel,
            userType = userType,
            saveId = chatViewModel.saveID,
            onClick = {
                navigateToChat()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String,
    chatViewModel: ChatViewModel = viewModel(),
    tesseractViewModel: TesseractViewModel,
    context: Context,
) {
    var messageText = remember { mutableStateOf(TextFieldValue("")) }
    var spweakmessageText = remember { mutableStateOf("") }
    var messages = chatViewModel.messages.observeAsState(emptyList())


   // tesseractViewModel.saveSateteButtonBrayl(context)
  //  if (tesseractViewModel.brauylTrue.value){

  //  }
    LaunchedEffect(chatId) {
        chatViewModel.loadMessages(chatId)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    // Инициализация TTS и привязка к жизненному циклу
    LaunchedEffect(lifecycleOwner) {
        tesseractViewModel.initializeTTS(context)
        tesseractViewModel.handleLifecycle(context, lifecycleOwner)

    }

  // tesseractViewModel.braylKeybord(spweakmessageText)
 //  tesseractViewModel.braylKeybord(messageText)


    val ststusTTs = ShedPreferences.getShedPreferences(
            context = context,
            UserFileCollections = ShedPreferences.CollectionsTTs,
            keyFile = ShedPreferences.keyTts)

    val ststusBrayl = ShedPreferences.getShedPreferences(
        context = context,
        UserFileCollections = ShedPreferences.CollectionsBrayl,
        keyFile = ShedPreferences.braylKey)
    if (ststusBrayl == ShedPreferences.yes) {
        LaunchedEffect(Unit) {
            tesseractViewModel.braylKeyboard(messageText)
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
                .background(BlueBlack)
        ) {


            LazyColumn(modifier = Modifier.weight(1f)) {
                items(messages.value) { message ->
                    if (message.senderId == chatViewModel.auchID.value) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = message.text,
                                style =  TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                color = BlueBlack),
                                modifier = Modifier
                                    .background(
                                        color = colorOlivical,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(12.dp)
                                    .widthIn(max = 180.dp)
                                    .clickable {
                                        if (message.text.isNotEmpty() && ststusTTs == ShedPreferences.yes) {
                                            tesseractViewModel.speakText(message.text)
                                        }
                                    }

                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = message.text,
                                style =  TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BlueBlack),
                                modifier = Modifier
                                    .background(Orange, shape = RoundedCornerShape(16.dp))
                                    .padding(12.dp)
                                    .widthIn(max = 180.dp)
                                    .clickable {
                                        if (message.text.isNotEmpty() && ststusTTs == ShedPreferences.yes) {
                                            tesseractViewModel.speakText(message.text)
                                        }
                                    }
                            )
                        }
                    }
                }
            }
            val focusManager = LocalFocusManager.current
            //tesseractViewModel.speakText(messageText.value.text)
            Row(modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically) {
              //  messageText.value = spweakmessageText.value
               /* if(messageText.value.text.isNotEmpty()) {
                    tesseractViewModel.speakText(messageText.value.text)
                }else{
                    Toast.makeText(context,"Текст пуст ", Toast.LENGTH_SHORT ).show()
                }*/

                OutlinedTextField(
                    value = messageText.value,
                    onValueChange = { messageText.value = it
                        if( messageText.value.text.isNotEmpty() && ststusTTs == ShedPreferences.yes) {
                            tesseractViewModel.speakText(messageText.value.text)
                        }},

                    singleLine = true,
                    textStyle = TextStyle(color = colorOlivical, fontSize = 16.sp),
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f)
                        .height(50.dp),
                    label = { Text("Сообщение") },
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done, keyboardType = KeyboardType.Text
                    ),
                            colors = TextFieldDefaults.outlinedTextFieldColors(

                            unfocusedBorderColor = chatViewModel.textLabelColor.value,
                    focusedBorderColor = chatViewModel.textLabelColorClick.value,
                    focusedLabelColor = chatViewModel.textLabelColor.value,
                    cursorColor = chatViewModel.textLabelColorClick.value,
                )
                )
                componetsRegistrations.IconButton(size =50.dp
                    , icon = Icons.Default.Send,
                    iconColor = colorOlivical) {
                    val newMessage = Message(
                        senderId = chatViewModel.auchID.value,
                        text = messageText.value.text,
                        timestamp = System.currentTimeMillis()
                    )
                    chatViewModel.sendMessage(chatId, newMessage)
                    tesseractViewModel.speakText(messageText.value.text)
                    messageText.value = TextFieldValue("")
                }
             /*   Button(onClick = {
                    val newMessage = Message(
                        senderId = chatViewModel.auchID.value,
                        text = messageText.value.text,
                        timestamp = System.currentTimeMillis()
                    )
                    chatViewModel.sendMessage(chatId, newMessage)
                    tesseractViewModel.speakText(messageText.value.text)
                    messageText.value = TextFieldValue("")


                }, modifier = Modifier
                    .height(50.dp)
                    .align(Alignment.CenterVertically)
                ) {
                    Text("Отправить")
                }*/
            }
        }
    }
}

/*
@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel,
    context: Context,
    userType: UserType,
) {
    var messageText = remember { mutableStateOf("") }
    val chatId = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        // Инициализация чата и наблюдение за сообщениями
        val existingChatId = chatViewModel.getChatIdForUsers(chatViewModel.auchID.value, chatViewModel.saveID.value)
        if (existingChatId != null) {
            chatId.value = existingChatId
            chatViewModel.observeMessages(existingChatId)
        } else {
            val newChatId = chatViewModel.createChat(listOf(chatViewModel.auchID.value, chatViewModel.saveID.value))
            chatId.value = newChatId
            chatViewModel.observeMessages(newChatId)
        }
    }

    chatId.value?.let { id ->
        LaunchedEffect(id) {
            chatViewModel.observeMessages(id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
            .background(BlueBlack)
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(chatViewModel.messages.value) { message ->
                if (message.senderId == chatViewModel.auchID.value) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = message.messageText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorOlivical,
                            modifier = Modifier
                                .background(Color.LightGray)
                                .padding(8.dp)
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = message.messageText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorOlivical,
                            modifier = Modifier
                                .background(Color.DarkGray)
                                .padding(8.dp)
                        )
                    }
                }
            }
        }

        val focusManager = LocalFocusManager.current
        Row(modifier = Modifier.padding(8.dp)) {
            OutlinedTextField(
                value = messageText.value,
                onValueChange = { messageText.value = it },
                singleLine = true,
                textStyle = TextStyle(color = colorOlivical, fontSize = 16.sp),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
                    .height(50.dp),
                label = { Text("Сообщение") },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Text
                )
            )
            Button(
                onClick = {
                    if (messageText.value.isNotEmpty() && chatId.value != null) {
                        val message = MassageUsers(
                            senderId = chatViewModel.auchID.value,
                            messageText = messageText.value,
                            timestamp = System.currentTimeMillis()
                        )
                        chatViewModel.getChatIdAndSendMessage(chatViewModel.auchID.value, chatViewModel.saveID.value, message)
                        messageText.value = ""
                        Log.d("ChatScreen", "Message sent: ${message.messageText}")
                    }
                },
                modifier = Modifier
                    .height(50.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text("Отправить")
            }
        }
    }
}*/
