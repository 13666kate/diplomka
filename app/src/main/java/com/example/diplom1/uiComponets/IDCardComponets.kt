package com.example.diplom1.uiComponets

import DataClass.UserCard
import DataClass.UserCardFriend
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.diplom1.ShedPreferences
import com.example.diplom1.ui.theme.Black
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.Grey
import com.example.diplom1.ui.theme.colorOlivical
import com.google.android.datatransport.cct.StringMerger
import com.google.firebase.firestore.auth.User
import firebase.FireBaseIDCardUser
import firebase.NameCollactionFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import org.checkerframework.checker.units.qual.Current
import screen.Row
import sence.kate.practica3.padding.Padding
import viewModel.CardVolonterViewModel
import viewModel.RegistrationViewModel
import viewModel.UserType

class IDCardComponets {


    @Composable()

    fun LazyColumnSearch(
        cardVolonterViewModel: CardVolonterViewModel,
        click: () -> Unit,
        context: Context,
        userType: UserType,
    ) {
        val status = ShedPreferences.getUserType(context)
        val list: MutableList<UserCard> = remember { mutableStateListOf() }

        if (status == userType.UserBlind.value) {
            list.clear()
            list.addAll(cardVolonterViewModel.uniqueVolo)
        } else if (status == userType.UserVolonters.value) {
            list.clear()
            list.addAll(cardVolonterViewModel.uniqueListBlind)
        }
        val items = list
        val filteredItems = items.filter { userdata ->
            userdata.surname.contains(
                cardVolonterViewModel.text.value,
                ignoreCase = true
            )
            userdata.name.contains(
                cardVolonterViewModel.text.value,
                ignoreCase = true
            )
            userdata.email.contains(
                cardVolonterViewModel.text.value,
                ignoreCase = true
            )
        }
        LazyColumn(
            modifier = Modifier
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            itemsIndexed(filteredItems) { index, userdata ->
                RowOne(
                    baground = colorOlivical,
                    imageSize = 75.dp,
                    name = userdata.name,
                    surname = userdata.surname,
                    bitmap = userdata.bitmap,
                    textSize = 18.sp,
                    textColor = BlueBlack,
                    email = userdata.email,
                    sizeEmail = 18.sp,
                    stateBitmap = cardVolonterViewModel.imageStateCardUser,
                    stateName = cardVolonterViewModel.nameStateCardUser,
                    stateSurname = cardVolonterViewModel.surnameStateCardUser,
                    stateEmail = cardVolonterViewModel.emailStateCardUser,
                    onClick = {
                        click()
                    })
            }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun LazyColumnUsers(
        cardVolonterViewModel: CardVolonterViewModel,
        context: Context,
        userType: UserType,
        click: () -> Unit
    ) {

        LaunchedEffect(Unit) {
            FireBaseIDCardUser().getUserList(context, cardVolonterViewModel, userType)
            // delay(1000)
        }
        //  cardVolonterViewModel.getList(context, cardVolonterViewModel, userType)
        val status = ShedPreferences.getUserType(context)
        val list: MutableList<UserCard> = remember { mutableStateListOf() }

        if (status == userType.UserBlind.value) {
            list.clear()
            list.addAll(cardVolonterViewModel.uniqueVolo)
        } else if (status == userType.UserVolonters.value) {
            list.clear()
            list.addAll(cardVolonterViewModel.uniqueListBlind)
        }
        LazyColumn(
            modifier = Modifier
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.Start,

            ) {
            itemsIndexed(list) { _, userdata ->

                Box(
                    modifier = Modifier

                ) {
                    //  cardVolonterViewModel.nameStateCardUser.value = userdata.name
                    RowOne(
                        baground = colorOlivical,
                        imageSize = 75.dp,
                        name = userdata.name,
                        surname = userdata.surname,
                        bitmap = userdata.bitmap,
                        textSize = 18.sp,
                        textColor = BlueBlack,
                        email = userdata.email,
                        sizeEmail = 18.sp,
                        stateBitmap = cardVolonterViewModel.imageStateCardUser,
                        stateName = cardVolonterViewModel.nameStateCardUser,
                        stateSurname = cardVolonterViewModel.surnameStateCardUser,
                        stateEmail = cardVolonterViewModel.emailStateCardUser,
                        onClick = {
                            click()
                        }
                    )
                }
            }
        }

    }

    @Composable
    fun LazyColumnList(
        context: Context,
        cardVolonterViewModel: CardVolonterViewModel,
        userType: UserType,
        onClick: () -> Unit,
    ) {
        val list: MutableList<UserCardFriend> = remember { mutableListOf<UserCardFriend>() }
        // Состояние для хранения данных списка друзей
        val friendListState = remember { mutableStateOf<List<UserCardFriend>>(emptyList()) }
        val status = ShedPreferences.getShedPreferences(
            context = context,
            UserFileCollections = ShedPreferences.FileCollectionsListFriend,
            keyFile = ShedPreferences.FileListAdd
        )

        LaunchedEffect(Unit){
            list.clear()
            val user = ShedPreferences.getUserType(context = context)
           // if (user == ShedPreferences.statusNoAuth.value){

         //   }
              //  cardVolonterViewModel.friendList.clear()

            //if (status == ShedPreferences.listAddYes) {
            val d =FireBaseIDCardUser().getFriendList(
                context, userType, cardVolonterViewModel)

                list.addAll(d)
            cardVolonterViewModel.removeDuplicatesUserCardsFriends(list,cardVolonterViewModel.friendList)

           //     cardVolonterViewModel.friendList.addAll(list)
             //   cardVolonterViewModel.friendList.addAll(list)
          //  } else {
           /* AddVolonter(
                navController = navController,
                nameScreen = nameScreenAddUser,*/

      //  }
        }
        val statusList = ShedPreferences.getShedPreferences(
            context, UserFileCollections = ShedPreferences.FileCollectionsListFriend,
            keyFile = ShedPreferences.FileListAdd
        )
        if (list.isEmpty()) {
            if (statusList == ShedPreferences.listAddYes) {
                ShedPreferences.saveShedPreferences(
                    context, UserFileCollections = ShedPreferences.FileCollectionsListFriend,
                    keyFile = ShedPreferences.FileListAdd,
                    value = ShedPreferences.listAddNo
                )
            }
        }

        // Состояние для списка


       /* if (status == ShedPreferences.listAddYes) {
            list.clear()
           list.addAll(dataList)
        } else {
            AddVolonter(
                navController = navController,
                nameScreen = nameScreenAddUser,
            )
        }*/

        LazyColumn(
            modifier = Modifier
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            itemsIndexed(cardVolonterViewModel.friendList) { _, userdata ->
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
                            onClick()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AddVolonter(
    navController: NavHostController,
    nameScreen: String,
    context: Context,
    userType: UserType,
) {
    val registrationViewModel = RegistrationViewModel()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BlueBlack)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier =
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
           /*  val status =ShedPreferences.getShedPreferences(
                 context = context,
                 UserFileCollections = ShedPreferences.FileCollectionsListFriend,
                 keyFile = ShedPreferences.FileListAdd)*/
            val status = ShedPreferences.getUserType(context)
            val textState = remember {
                mutableStateOf("")
            }
            if (status == userType.UserBlind.value) {
                textState.value = "У вас еще нет волонтера!"
            }else if(status == userType.UserVolonters.value){
                textState.value = "У вас еще нет подопечного!"

            }
            Text(
                text = textState.value,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Grey,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
            Text(
                text = "Нажмите, чтобы добавить!",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Grey,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
            Box(modifier = Modifier.padding(20.dp)) {

                ComponetsRegistrations().IconButton(
                    size = 100.dp,
                    icon = Icons.Default.AddCircle,
                    iconColor = colorOlivical,
                    onClick = {
                        navController.navigate(nameScreen)
                    }
                )

            }
        }

    }
}
@Composable
fun RowOne(
    baground: Color,
    imageSize: Dp,
    name: String,
    surname: String,
    bitmap: Bitmap?,
    textColor: Color,
    textSize: TextUnit,
    email: String,
    stateEmail: MutableState<String>,
    stateName: MutableState<String>,
    stateSurname: MutableState<String>,
    stateBitmap: MutableState<Bitmap?>,
    sizeEmail: TextUnit,
    onClick: () -> Unit

) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            //.clip(shape = CircleShape)
            .clickable {
                onClick()

                stateBitmap.value = bitmap
                stateName.value = name
                stateSurname.value = surname
                stateEmail.value = email
            }
            .background(
                color = baground, shape = CircleShape
            ),
    )

    {

        Box(
            modifier = Modifier
                .padding(5.dp)
                .border(
                    width = Padding.border,
                    brush = Brush
                        .linearGradient(
                            colors = listOf(Black, Black)
                        ),
                    shape = CircleShape
                )

        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "imageStore",
                    modifier = Modifier
                        .size(imageSize)
                        .clip(CircleShape)
                        .padding(start = 5.dp, top = 5.dp, bottom = 5.dp),
                    contentScale = ContentScale.Crop,

                    )
            }else{
                Image(
                   imageVector = Icons.Default.AccountCircle,
                    contentDescription = "imageStore",
                    modifier = Modifier
                        .size(imageSize)
                        .clip(CircleShape),
                      //  .padding(start = 5.dp, top = 5.dp, bottom = 5.dp, end = ),
                    contentScale = ContentScale.Crop,

                    )
            }
        }
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.padding(
                    start = 5.dp,
                    top = 5.dp
                )
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 10.dp),
                    text = name,
                    style = TextStyle(
                        color = textColor,
                        textAlign = TextAlign.Center,
                        fontSize = textSize,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = surname,
                    style = TextStyle(
                        color = textColor,
                        textAlign = TextAlign.Center,
                        fontSize = textSize,
                        fontWeight = FontWeight.Bold
                    )

                )
            }
            Text(
                text = email,
                modifier = Modifier
                    .padding(top = 15.dp),
                style = TextStyle(
                    color = textColor,
                    textAlign = TextAlign.Center,
                    fontSize = sizeEmail,
                    fontWeight = FontWeight.Bold
                )

            )

        }


    }
}
@Composable
fun RowTwo(
    baground: Color,
    imageSize: Dp,
    name: String,
    surname: String,
    bitmap: Bitmap?,
    textColor: Color,
    textSize: TextUnit,
    email: String,
    aboutMe:String,
    rayon:String,
    region:String,
    adress:String,
    yersVolonters:String,
    birthday:String,
    number:String,
    stateEmail: MutableState<String>,
    stateName: MutableState<String>,
    stateSurname: MutableState<String>,
    stateBitmap: MutableState<Bitmap?>,
    stateRayon:MutableState<String>,
    stateRegion:MutableState<String>,
    stateAddress:MutableState<String>,
    stateVoloYesrs:MutableState<String>,
    stateBirthday:MutableState<String>,
    stateNumber:MutableState<String>,
    stateAboutMe:MutableState<String>,
    sizeEmail: TextUnit,
    onClick: () -> Unit

) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            //.clip(shape = CircleShape)
            .clickable {
                onClick()

                stateBitmap.value = bitmap
                stateName.value = name
                stateSurname.value = surname
                stateEmail.value = email
                stateAddress.value = adress
                stateBirthday.value = birthday
                stateRegion.value = region
                stateRayon.value = rayon
                stateVoloYesrs.value = yersVolonters
                stateAboutMe.value = aboutMe
                stateNumber.value = number

            }
            .background(
                color = baground, shape = CircleShape
            ),
    )

    {

        Box(
            modifier = Modifier
                .padding(5.dp)
                .border(
                    width = Padding.border,
                    brush = Brush
                        .linearGradient(
                            colors = listOf(Black, Black)
                        ),
                    shape = CircleShape
                )

        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "imageStore",
                    modifier = Modifier
                        .size(imageSize)
                        .clip(CircleShape)
                        .padding(start = 5.dp, top = 5.dp, bottom = 5.dp),
                    contentScale = ContentScale.Crop,

                    )
            }else{
                Image(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "imageStore",
                    modifier = Modifier
                        .size(imageSize)
                        .clip(CircleShape),
                    //  .padding(start = 5.dp, top = 5.dp, bottom = 5.dp, end = ),
                    contentScale = ContentScale.Crop,

                    )
            }
        }
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.padding(
                    start = 5.dp,
                    top = 5.dp
                )
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 10.dp),
                    text = name,
                    style = TextStyle(
                        color = textColor,
                        textAlign = TextAlign.Center,
                        fontSize = textSize,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = surname,
                    style = TextStyle(
                        color = textColor,
                        textAlign = TextAlign.Center,
                        fontSize = textSize,
                        fontWeight = FontWeight.Bold
                    )

                )
            }
            Text(
                text = email,
                modifier = Modifier
                    .padding(top = 15.dp),
                style = TextStyle(
                    color = textColor,
                    textAlign = TextAlign.Center,
                    fontSize = sizeEmail,
                    fontWeight = FontWeight.Bold
                )

            )

        }


    }
}



