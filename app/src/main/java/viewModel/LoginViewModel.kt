package viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.ui.theme.Orange


class LoginViewModel: ViewModel() {
    val olivicalColor = Color(Orange.value)
    val login = mutableStateOf("")//сохраняем сотояние для login
    val Password = mutableStateOf("")//сохраняем сотояние для password
    var textLabelColor: MutableState<Color> = mutableStateOf(colorOlivical)//Устанавливаем цвет Водда
    var textLabelColorClick: MutableState<Color> =
        mutableStateOf(Orange)//устанавливаем цвет при нажатии на ввод
    var textColorClick: MutableState<Color> =
        mutableStateOf(Orange)//устанавливаем цвет при нажатии на ввод
    var textColor: MutableState<Color> =
        mutableStateOf(colorOlivical) //устанавливаем цвет при нажатии на ввод

    private val _isClicked = mutableStateOf(false)

    //_____________________________________________________________________________
            //при нажатии на текст "Еще не зарегистрированы", меняем его цвет
    fun setTextColor() {
    //_isClicked.value = true
        textColor.value = if (_isClicked.value) Color(Orange.value) else Color(colorOlivical.value)
    }
    //_____________________________________________________________________________
}

