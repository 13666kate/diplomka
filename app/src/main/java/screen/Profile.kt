package screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.diplom1.ShedPreferences
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.Red
import com.example.diplom1.uiComponets.ComponetsRegistrations
import firebase.FireBaseIDCardUser
import kotlinx.coroutines.flow.first
import viewModel.CardVolonterViewModel
import viewModel.LoginViewModel
import viewModel.UserType

@Composable
fun Profile(context: Context,
            userType: UserType,
            navController: NavController,
            loginViewModel:LoginViewModel,
            cardVolonterViewModel: CardVolonterViewModel,
            nameScreen:String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBlack),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
 // userType.user(context, navController, nameScreen,cardVolonterViewModel,userType)
    //   var user = cardVolonterViewModel.listUserType.collectAsState(initial = cardVolonterViewModel.userCards)
        ComponetsRegistrations().IconButton(
            size = 100.dp, icon = Icons.Default.Close,
            iconColor = Red,
            onClick = {

                    // Очищаем текущий список пользователей
                  //  cardVolonterViewModel.uniqueList=user.value.toMutableList()
                userType.user(context, navController, nameScreen,cardVolonterViewModel,userType)
                    // Получаем новый список пользователей

                    // Вызываем ваш метод обновления пользовательского интерфейса
                //     cardVolonterViewModel.listUser.addAll(user)

                    // Сохраняем статус пользователя
                    ShedPreferences.saveUserType(context = context, userType = ShedPreferences.statusNoAuth.value)


            }
            )


            }


        }



/*
@Composable
@Preview
fun MyScreen(context: Context) {

    Profile(context = context, UserType())
}*/
