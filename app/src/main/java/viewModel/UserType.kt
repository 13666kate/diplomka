package viewModel

import DataClass.UserCard
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.diplom1.ScreenName.ScreenName
import com.example.diplom1.ShedPreferences
import firebase.FireBaseIDCardUser
import firebase.NameCollactionFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch


class UserType : ViewModel() {
    val userType= mutableStateOf(false)
    val statusUser: MutableState<String> = mutableStateOf("")
    val statusUserRegistrations: MutableState<String> = mutableStateOf("")


    var UserBlind: MutableState<String> = mutableStateOf("Пользователь ")
    var UserVolonters: MutableState<String> = mutableStateOf("Волонтер")


    fun userTypeTrue() {

        userType.value = true

    }

    fun userTypeFalse() {

       userType.value = false

    }

    fun user(
        context: Context,
        navController: NavController,
        nameScreen: String,
        cardVolonterViewModel: CardVolonterViewModel,
        userType: UserType,
    ) {
        viewModelScope.launch {
            val current = ShedPreferences.getUserType(context)
            val curentStatus = getUser(context)
            if (current != ShedPreferences.statusNoAuth.value) {
                navController.navigate(nameScreen) {
                    popUpTo(nameScreen)
                }
                ShedPreferences.saveUserType(
                    context,
                    userType = ShedPreferences.statusNoAuth.value
                )
             //   cardVolonterViewModel.uniqueList.clear()



            }
            FireBaseIDCardUser().getUserList(context, cardVolonterViewModel, userType)
            Toast.makeText(context, curentStatus, Toast.LENGTH_SHORT).show()
        }
    }


    fun getUser(context: Context): String {
        viewModelScope.launch {
            val currentUserType = ShedPreferences.getUserType(context = context)
            if (currentUserType != null) {
                statusUser.value = currentUserType

            }

        }
        return statusUser.value
    }
    fun getUserRegistration(context: Context): String {
        viewModelScope.launch {
            val currentUserType = ShedPreferences.getUserTypeStatus(context = context)
            if (currentUserType != null) {
               statusUserRegistrations.value = currentUserType
            }

        }
        return statusUserRegistrations.value
    }

}