package screen

import DataClass.UserCardAdd
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.uiComponets.RowOne
import viewModel.CardVolonterViewModel
import viewModel.UserType
import  androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.ui.draw.scale
import com.example.diplom1.ui.theme.Orange
import  androidx.compose.material3.Text
import com.example.diplom1.ShedPreferences


@Composable
fun NotificationsUserAdd(
    cardVolonterViewModel: CardVolonterViewModel,
    onclickListAdd: () -> Unit,
    onclickListSee:()->Unit,
    context: Context,
    userType: UserType,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BlueBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
        ) {
            val userList = remember { mutableStateListOf<UserCardAdd>() }


            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.padding(top = 15.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                ) {
                    RadioButton(
                        selected = cardVolonterViewModel.radioButton.value,
                        onClick = {
                            ShedPreferences.saveShedPreferences(
                                context = context,
                                UserFileCollections = ShedPreferences.FileCollectionsList,
                                keyFile = ShedPreferences.keylistUserAdd,
                                value = ShedPreferences.keylistUserAdd
                            )
                            userList.clear()
                            cardVolonterViewModel.radioButton.value = true

                        },
                        modifier = Modifier.scale(1.3f),
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colorOlivical,
                            unselectedColor = Orange
                        )
                    )
                    androidx.compose.material3.Text(
                        color = colorOlivical,
                        text = "Входящие",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                ) {
                    RadioButton(
                        selected = !cardVolonterViewModel.radioButton.value,
                        onClick = {
                            userList.clear()
                            ShedPreferences.saveShedPreferences(
                                context = context,
                                UserFileCollections = ShedPreferences.FileCollectionsList,
                                keyFile = ShedPreferences.keylistUserAdd,
                                value = ShedPreferences.listUserSee
                            )
                            cardVolonterViewModel.radioButton.value = false
                        },
                        modifier = Modifier.scale(1.3f),
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colorOlivical,
                            unselectedColor = Orange
                        )
                    )
                    Text(
                        color = colorOlivical,
                        text = "Исходящие",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }


            if (cardVolonterViewModel.radioButton.value == true) {
                userList.clear()

                val list = cardVolonterViewModel.NotificationsUserAdd(
                    cardVolonterViewModel = cardVolonterViewModel,
                    context = context,
                    userType = userType,
                )
                userList.addAll(list)
            }
                if (cardVolonterViewModel.radioButton.value == false) {

                    userList.clear()

                    val listAdd = cardVolonterViewModel.UserViewingAddMe(
                        cardVolonterViewModel = cardVolonterViewModel,
                        context = context,
                        userType = userType,
                    )
                    userList.addAll(listAdd)
                }



                LazyColumn(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    horizontalAlignment = Alignment.Start,
                ) {

                    itemsIndexed(userList) { _, userdata ->
                        Box(modifier = Modifier) {
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
                                       if (cardVolonterViewModel.radioButton.value) {
                                    onclickListAdd()

                                       }else{
                                           onclickListSee()
                                       }
                                }
                            )
                        }
                    }
                }

            }
        }

    }

    fun CheckForIntersections(
        list1: MutableList<UserCardAdd>,
        list2: MutableList<UserCardAdd>,
        list: MutableList<UserCardAdd>
    ): MutableList<UserCardAdd> {
// Находим пересечения
        val intersection = list1.intersect(list2)

        // Удаляем пересечения из обоих списков
        if (intersection.isNotEmpty()) {
            list1.removeAll(intersection)
            list.addAll(list1)
        }

        return list
    }


/*
@Composable
@Preview
fun MyScreen(){
    NotificationsUserAdd(CardVolonterViewModel(), onclickList = {}, userType = UserType())
}
*/
