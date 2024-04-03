package com.example.diplom1.registration.mvvm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.diplom1.ui.theme.Olivical
import com.example.diplom1.ui.theme.OlivicalBlue

class RegistrationViewModel: ViewModel() {

    val login = mutableStateOf("")
    val password = mutableStateOf("")
    var textLabelColor: MutableState<Color> = mutableStateOf(Color(Olivical.value))
    var textLabelColorClick: MutableState<Color> = mutableStateOf(OlivicalBlue)//устанавливаем цвет при нажатии на ввод
}