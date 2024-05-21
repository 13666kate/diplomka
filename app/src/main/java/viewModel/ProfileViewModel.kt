package viewModel

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import firebase.FireBaseIDCardUser
import firebase.FirebaseProfile
import kotlinx.coroutines.launch

class ProfileViewModel () :ViewModel(){
    val nameUser = mutableStateOf("Имя")
    val surnameUser = mutableStateOf("Фамилия")
    val email = mutableStateOf("Email")
    val address = mutableStateOf("Адрес")
    val phone = mutableStateOf("Телефон")
    val rayon = mutableStateOf("Район")
    val region = mutableStateOf("Регион")
    val birhday = mutableStateOf("День рождение")
    val aboutMe = mutableStateOf("О себе")
    val yersVolonters = mutableStateOf("Годы в волонтерстве")
    val bitmap:MutableState<Bitmap?> = mutableStateOf(null)

    fun getData(userType:UserType,
                context: Context,
                cardVolonterViewModel: CardVolonterViewModel){
        viewModelScope.launch {
            FirebaseProfile().getData(
                name = nameUser,
                surname = surnameUser,
                email=email,
                bitmap = bitmap,
                birhday = birhday,
                adres = address,
                region = region,
                rayon = rayon,
                experienceVolonters = yersVolonters,
                number = phone,
                aboutMe = aboutMe,
                userType = userType,
                context = context,
                cardVolonterViewModel = cardVolonterViewModel

            )
        }
    }

}