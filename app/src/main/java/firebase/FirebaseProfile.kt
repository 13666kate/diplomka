package firebase

import android.content.Context
import android.graphics.Bitmap
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.diplom1.ShedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import viewModel.CardVolonterViewModel
import viewModel.UserType

class FirebaseProfile {
    suspend fun getData(
        name: MutableState<String>,
        surname: MutableState<String>,
        email: MutableState<String>,
        bitmap: MutableState<Bitmap?>,
        birhday: MutableState<String>,
        adres: MutableState<String>,
        region: MutableState<String>,
        rayon: MutableState<String>,
        experienceVolonters: MutableState<String>,
        aboutMe: MutableState<String>,
        number: MutableState<String>,
        context: Context,
        userType: UserType,
        cardVolonterViewModel: CardVolonterViewModel
    ) = coroutineScope {
        launch {


            val uid = FirebaseRegistrations().userID()

            val statusUser = ShedPreferences.getUserType(context)
            val nameCollections = if (statusUser == userType.UserBlind.value) {
                NameCollactionFirestore.UsersBlind
            } else {
                NameCollactionFirestore.UsersVolonters
            }
            val document =
                FirebaseFirestore.getInstance()
                    .collection(nameCollections)
                    .document(uid.toString())
                    .get().await()

            val pathBitmap = FireBaseIDCardUser().storageFireStore(uid.toString())
            if (pathBitmap.isEmpty()) {
                Log.e("list", "КАртинка пуста ")
            }
            val bitmapImage = cardVolonterViewModel.loadFirebaseImage(pathBitmap)
            name.value = document.getString(FirebaseString.name).toString()
            surname.value = document.getString(FirebaseString.surname).toString()
            email.value = document.getString(FirebaseString.email).toString()
            bitmap.value = bitmapImage
            adres.value = document.getString(FirebaseString.adress).toString()
            region.value = document.getString(FirebaseString.region).toString()
            rayon.value = document.getString(FirebaseString.rayon).toString()
            aboutMe.value = document.getString(FirebaseString.aboutMe).toString()
            experienceVolonters.value = document.getString(FirebaseString.experience).toString()
            number.value = document.getString(FirebaseString.phone).toString()
            birhday.value = document.getString(FirebaseString.birdhday).toString()
        }
    }
}