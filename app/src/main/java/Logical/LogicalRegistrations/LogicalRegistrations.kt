package Logical.LogicalRegistrations

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.ui.theme.Red
import viewModel.RegistrationViewModel
import java.text.SimpleDateFormat
import java.util.Date


public class  LogicalRegistrations( val registrationViewModel: RegistrationViewModel) {

    fun clueLogin(textEmail:MutableState<String>,
                  textColor:MutableState<Color> ) {

        var text = textEmail.value
        val containsUpperCase = !text.any { it.isUpperCase()}
            if(textEmail.value.isEmpty() || (textEmail.value.contains(' ') || containsUpperCase )) {
            textColor.value= Red
        }else{
            textColor.value= colorOlivical
        }
            //registrationViewModel.isButtonEnabled.value = false
        }

    fun cluePassword(textPassword:MutableState<String>,
                     textColor:MutableState<Color> ) {

        var text = textPassword.value
        val containsNumber = !text.any { it.isDigit()}
        if(textPassword.value.isEmpty() || containsNumber ) {
            textColor.value= Red
        }else{
            textColor.value= colorOlivical
        }
        //registrationViewModel.isButtonEnabled.value = false
    }
    fun clueEmail(textEmail:MutableState<String>,
                  textColor:MutableState<Color> ) {

        var text = textEmail.value
        val containsNumber = !text.contains('@')
        val endsWithWord = !text.endsWith("@gmail.com")
        if(textEmail.value.isEmpty()  || endsWithWord) {
            textColor.value= Red
        }else{
            textColor.value= colorOlivical
        }
        //registrationViewModel.isButtonEnabled.value = false
    }
    fun clueLastName(textLastName:MutableState<String>,
                  textColor:MutableState<Color> ) {

        var text = textLastName.value
        val containsUpperCase = !text.any { it.isUpperCase()}
        val containsNumber = text.any { it.isDigit()}
        val containsSymbol = text.any { !it.isLetterOrDigit() }

        if(textLastName.value.isEmpty()  || containsNumber ||  containsSymbol || !text[0].isUpperCase()) {
            textColor.value= Red
        }else{

            textColor.value= colorOlivical
        }
        //registrationViewModel.isButtonEnabled.value = false
    }
    fun clueNumber(textNumber:MutableState<String>,
                   textColor:MutableState<Color> ) {

        var text = textNumber.value
  /*      val containsNumber = !text.all{ it.isDigit()}
        val containsSimbol = text.any { !it.isLetterOrDigit() }
        val containsLowerCase =  text.any { it.isLowerCase() }
        val containsUpperCase =  text.any { it.isUpperCase() }*/
        val isCorrectFormat = checkPhoneNumberFormat(text)
        if(textNumber.value.isEmpty()  || !isCorrectFormat) {
                textColor.value= Red
        }else{

            textColor.value= colorOlivical
        }
        //registrationViewModel.isButtonEnabled.value = false
    }
    fun clueDate(dataState:MutableState<String>,
                 textColorDate:MutableState<Color> ) {

        var text = dataState.value
        val isCorrectFormat = checkPhoneNumberFormat(text)
        if(dataState.value.isEmpty()  || !isValidDateFormat(text)) {
            textColorDate.value= Red
        }else{

            textColorDate.value= colorOlivical
        }
        //registrationViewModel.isButtonEnabled.value = false
    }
    fun checkPhoneNumberFormat(phoneNumber: String): Boolean {
        val regex = Regex("^\\+996\\d{9}$")
        return regex.matches(phoneNumber)
    }
    fun isValidDateFormat(text: String): Boolean {
        val regex = Regex("^\\d{1,2}\\.\\d{1,2}\\.\\d{4}\$")
        //проверка верен ли шаблон
        if (!regex.matches(text)) {
            return false
        }
        // разбиваем нашу строку на части
        // ориентируемся точкой
        val parts = text.split(".")
        val day = parts[0].toIntOrNull()
        val month = parts[1].toIntOrNull()
        val year = parts[2].toIntOrNull()

        // Проверка корректности значений дня, месяца и года
        //не пусты ли значения
        if (day == null || month == null || year == null) {
            return false
        }
        // проверка  день меясц и год  в правильном диапозоне
        if (day !in 1..31 || month !in 1..12 || year < 0) {

            return false
        }
       // если это февраль то проверка на высокосный год
        val maxDay = when (month) {
            2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        // апрель июнь сентябрь ноябрь имеют по 30 дней
            4, 6, 9, 11 -> 30
            // все остальные по 31
            else -> 31
        }
        if (day !in 1..maxDay) {
            return false
        }

        return true
    }


    fun convertLongToTime(time: Long): String{
       val date = Date(time)
        val format = SimpleDateFormat("d.MM.yyyy")
        return format.format(date)
    }
}