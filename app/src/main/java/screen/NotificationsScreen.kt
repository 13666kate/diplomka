package screen

import DataClass.UserCard
import DataClass.UserCardAdd
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.uiComponets.RowOne
import viewModel.CardVolonterViewModel
import viewModel.UserType


@Composable
fun NotificationsUserAdd(
    cardVolonterViewModel: CardVolonterViewModel,
    onclickList:()->Unit,
    context: Context,
    userType: UserType,
){
 Column(modifier = Modifier
     .fillMaxSize()
     .background(color = BlueBlack)
 ) {

    val list = cardVolonterViewModel.NotificationsUserAdd(
         cardVolonterViewModel= cardVolonterViewModel,
        context = context,
        userType=userType,
     )
     /*val listUser:MutableList<UserCardAdd> = remember { mutableStateListOf() }
     listUser.add(list)*/
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
                         onclickList()
                     }
                 )
             }
         }
     }


 }
}

/*
@Composable
@Preview
fun MyScreen(){
    NotificationsUserAdd()
}*/
