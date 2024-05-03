package viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.diplom1.ui.theme.colorOlivical

class UserType: ViewModel() {
    //если FALSE То это волонтер иначе пользователь
  // val userType = mutableStateOf(false)
    //val userType = mutableStateOf(false)
    var userType= mutableStateOf(false)


    fun isUsertrue(){
        userType.value = true
    }
    fun isUserFalse(){
        userType.value = false
    }
}