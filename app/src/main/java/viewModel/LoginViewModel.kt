package viewModel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.diplom1.R
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.ui.theme.Orange
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import firebase.FirebaseRegistrations
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class LoginViewModel : ViewModel() {
    val olivicalColor = Color(Orange.value)
    val login = mutableStateOf("")//сохраняем сотояние для login
    val Password = mutableStateOf("")//сохраняем сотояние для password
    var textLabelColor: MutableState<Color> =
        mutableStateOf(colorOlivical)//Устанавливаем цвет Водда
    var textLabelColorClick: MutableState<Color> =
        mutableStateOf(Orange)//устанавливаем цвет при нажатии на ввод
    var textColorClick: MutableState<Color> =
        mutableStateOf(Orange)//устанавливаем цвет при нажатии на ввод
    var textColor: MutableState<Color> =
        mutableStateOf(colorOlivical) //устанавливаем цвет при нажатии на ввод
    private val _isClicked = mutableStateOf(false)
    var textAuthenticationsValue: MutableState<Int> = mutableStateOf(0)
    var ColorOtline: MutableState<Color> = mutableStateOf(colorOlivical)


    //_____________________________________________________________________________
    //при нажатии на текст "Еще не зарегистрированы", меняем его цвет
    fun setTextColor() {
        //_isClicked.value = true
        textColor.value = if (_isClicked.value) Color(Orange.value) else Color(colorOlivical.value)
    }
    //_____________________________________________________________________________

    fun userAuthentication(
        email: MutableState<String>,
        password: MutableState<String>,
        navController: NavHostController,
        textNoRegistrations: MutableState<Int>,
        collectionsFireStore: String,
        colorOutline: MutableState<Color>,
        context: Context,
        nameScreenNavigations: String,
       // homeScreenViewModel: HomeScreenViewModel,

    ) {
        viewModelScope.launch() {
            FirebaseRegistrations().userAuthentication(
                email = email.value,
                password = password.value,
                navController = navController,
                textNoRegistrations = textNoRegistrations,
                collectionsFireStore = collectionsFireStore,
                colorOutline = colorOutline,
                context = context,
                nameScreenNavigations = nameScreenNavigations,
                //homeScreenViewModel = homeScreenViewModel
            )
        }

    }
}




