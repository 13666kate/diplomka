package viewModel


import DataClass.BottomBarScreen
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.example.diplom1.R
import com.example.diplom1.ShedPreferences
import com.example.diplom1.ui.theme.colorOlivical

class BottomNavigationViewModel: ViewModel() {

    val colorClickIcon :MutableState<Color> = mutableStateOf(colorOlivical)
    val volonter = mutableStateOf(BottomBarScreen.Volonter.route)
    val statusScreen = mutableStateOf("")


    fun ScreenIcon(userType: UserType,context:Context):BottomBarScreen{
        val statusUser = ShedPreferences.getUserType(context)
        if (statusUser == userType.UserBlind.value) {
           return BottomBarScreen.Volonter
        } else if (statusUser == userType.UserVolonters.value){
            return BottomBarScreen.User
        }
        return BottomBarScreen.User
    }

    fun Screen(userType: UserType,context:Context):String{
        val statusUser = ShedPreferences.getUserType(context)
        if (statusUser == userType.UserBlind.value) {
            return BottomBarScreen.Volonter.route
        } else if (statusUser == userType.UserVolonters.value){
            return BottomBarScreen.User.route
        }
        return userType.UserVolonters.value
    }
}
