package com.example.diplom1.login.mvvm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.diplom1.ui.theme.Olivical
import com.example.diplom1.ui.theme.OlivicalBlue
import com.example.diplom1.ui.theme.OlivicalWhite


class LoginViewModel: ViewModel() {
    val olivicalColor = Color(OlivicalBlue.value)
    val login = mutableStateOf("")//сохраняем сотояние для login
    val Password = mutableStateOf("")//сохраняем сотояние для password
    var textLabelColor: MutableState<Color> = mutableStateOf(Olivical)//Устанавливаем цвет Водда
    var textLabelColorClick: MutableState<Color> =
        mutableStateOf(OlivicalBlue)//устанавливаем цвет при нажатии на ввод
    var textColorClick: MutableState<Color> =
        mutableStateOf(OlivicalBlue)//устанавливаем цвет при нажатии на ввод
    var textColor: MutableState<Color> =
        mutableStateOf(Olivical) //устанавливаем цвет при нажатии на ввод

    private val _isClicked = mutableStateOf(false)

    //_____________________________________________________________________________
            //при нажатии на текст "Еще не зарегистрированы", меняем его цвет
    fun setTextColor() {
    //_isClicked.value = true
        textColor.value = if (_isClicked.value) Color(OlivicalBlue.value) else Color(Olivical.value)
    }
    //_____________________________________________________________________________
}

