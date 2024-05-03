package firebase

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.diplom1.R
import com.example.diplom1.ui.theme.Red
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import viewModel.HomeScreenViewModel
import viewModel.RegistrationViewModel
import viewModel.UserType

class FirebaseRegistrations {
    fun registerUser(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        context:
        Context,
        name: String,
        surname: String,
        login: String,
        email: String,
        password: String,
        imageUri: Uri?,
        pinCard: String,
        idCard: String,
        documentName: String,
        birdhday: String,
        phone: String,
        registrationViewModel: RegistrationViewModel,
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val userData = mapOf(
                            FirebaseString.name to name,
                            FirebaseString.surname to surname,
                            FirebaseString.email to email,
                            FirebaseString.login to login,
                            FirebaseString.password to password,
                            FirebaseString.image to imageUri,
                            FirebaseString.pinCard to pinCard,
                            FirebaseString.idCard to idCard,
                            FirebaseString.birdhday to birdhday,
                            FirebaseString.phone to phone
                        )
                        firestore.collection(documentName).document(userId)
                            .set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context, R.string.SucsesfulRegistrations,
                                    Toast.LENGTH_SHORT
                                ).show()
                                registrationViewModel.cleareState()
                            }
                            .addOnFailureListener { e ->
                                Log.e(ContentValues.TAG, "Ошибка:${e.message}")
                            }
                    }
                    if (imageUri != null) {
                        val imageRef = storage.reference.child("images/${userId}/profile.jpg")
                        imageRef.putFile(imageUri)
                            .addOnSuccessListener {
                                imageRef.downloadUrl.addOnSuccessListener { uri ->
                                    // Получаем URL изображения в виде строки
                                    registrationViewModel.imageUriString.value = uri.toString()
                                    Log.e(ContentValues.TAG, "картинка загружена")
                                }
                                    .addOnFailureListener { e ->
                                        Log.e(
                                            ContentValues.TAG,
                                            "Ошибка загрузки картинки : ${e.message}"
                                        )
                                    }
                            }

                    } else {
                        Log.e(ContentValues.TAG, "Ошибка user ID ")
                    }
                } else {
                    Toast.makeText(
                        context, "Ошибка при регитсрации: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun userAuthentication(
        email: String,
        password: String,
        navController: NavHostController,
        textNoRegistrations: MutableState<Int>,
        collectionsFireStore: String,
        colorOutline: MutableState<Color>,
        context: Context,
        nameScreenNavigations:String

    ) {
        if (email.isEmpty() || password.isEmpty()) {
            colorOutline.value = Red
            Toast.makeText(context, "Заполните поля", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val db = FirebaseFirestore.getInstance()

                    db.collection(collectionsFireStore).document(currentUser?.uid ?: "")
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val firestorePassword = document.getString(FirebaseString.password)
                                val firestoreEmail = document.getString(FirebaseString.email)
                                if (firestoreEmail == email) {
                                    navController.navigate(nameScreenNavigations)

                                } else {
                                    // Пароль не совпадает
                                 //   Toast.makeText(context, "Неверный логин или пароль ${UserType().userType.value}",
                                    //    Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                // Документ не найден
                               // Toast.makeText(context, "Данные пользователя не найдены${UserType().userType.value}", Toast.LENGTH_SHORT).show()
                            }

                        }
                    /* FirebaseFirestore.getInstance().collection(collectionsFireStore).get()
                         .addOnSuccessListener { documents ->
                             for (document in documents) {
                                 val registrationsUserPassword = document.getString("password")
                                 val registrationsUserEmail = document.getString("email")
                                 if (password.isNotEmpty() && email.isNotEmpty()) {
                                     if (registrationsUserPassword == password
                                         && registrationsUserEmail == email
                                     ) {
                                         navController.navigate(nameScreenNavigations)
                                         // завершаем работу кода
                                         return@addOnSuccessListener
                                     }
                                 } else {
                                     colorOutline.value = Red
                                     Toast.makeText(context, "Заполните поля", Toast.LENGTH_SHORT)
                                         .show()
                                 }
                             }
                         }*/
                } else {
                    val error = task.exception
                    if (error is FirebaseAuthInvalidUserException) {
                        textNoRegistrations.value = R.string.noRegistrations
                        Toast.makeText(context, R.string.noRegistrations, Toast.LENGTH_SHORT)
                            .show()
                    } /*else if (error is FirebaseAuthInvalidCredentialsException) {
                        textNoRegistrations.value = R.string.ErrorPassword
                        Toast.makeText(context,  R.string.ErrorPassword, Toast.LENGTH_SHORT)
                            .show()
                    }*/
                }
            }
    }

    fun FirestoreDataUser(
        collectionsFireStore: String,
        imageState: MutableState<Uri?>,
        name: MutableState<String>,
        surname: MutableState<String>,
        email: MutableState<String>,
        phone: MutableState<String>,
        birdhday: MutableState<String>,
        idCard: MutableState<String>,
        pinCard: MutableState<String>,
        homeScreenViewModel: HomeScreenViewModel,
        navController: NavHostController,
        context: Context,
        nameScreenNavigations: String,


        ) {//тут может быть ошибка

        if (userID() != null) {
            //homeScreenViewModel.uidUserState.value = uid
            // navController.navigate(nameScreenNavigations)
            //    Toast.makeText(context, uid, Toast.LENGTH_SHORT).show()

            FirebaseFirestore.getInstance().collection(collectionsFireStore).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {

                    }
                    /*FirebaseFirestore.getInstance().collection(collectionsFireStore).get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                if (storageFireStore(imageState) != null){
                    imageState.value = storageFireStore(imageState = imageState)
                }else{
                    imageState.value =
                }
               imageState.value = document.getString(FirebaseString.image)
                val name = document.getString(FirebaseString.name)
                val surname = document.getString(FirebaseString.surname)
                val pinCard = document.getString(FirebaseString.pinCard)
                val idCard = document.getString(FirebaseString.idCard)
                val birdhday  = document.getString(FirebaseString.birdhday)
                if (password.isNotEmpty() && email.isNotEmpty()) {
                    if (registrationsUserPassword == password
                        && registrationsUserEmail == email
                    ) {
                        navController.navigate(nameScreenNavigations)
                        // завершаем работу кода
                        return@addOnSuccessListener
                    }
                } else {
                    colorOutline.value = Red
                    Toast.makeText(context, "Заполните поля", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
} else {
    Toast.makeText(context, R.string.NoAcсount, Toast.LENGTH_SHORT).show()
}*/

                }
        }
    }


    fun fireStoreName( name:MutableState<String>){

    }

// вытаскиваем картику из Storage


    //функция для получения пути к картинку
    fun storageFireStore(): String {
        val userId = userID()
        val storage = FirebaseStorage.getInstance().reference
        val image = storage.child("images/${userId}/profile.jpg")
        return image.path
    }

    fun userID(): String? {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val uid = currentUser?.uid // ID usera для заполнения анкеты
        return uid
    }

    //функция для получения картинки со Storage
    suspend fun loadFirebaseImage(path: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val storageRef = FirebaseStorage.getInstance().getReference(path)
                // Максимальный размер файла для загрузки (в данном случае 1 MB)
                val maxDownloadSizeBytes: Long = 7*1024 * 1024

                // Получение массива байтов изображения
                val byteArray = storageRef.getBytes(maxDownloadSizeBytes).await()

                // Преобразование массива байтов в Bitmap
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            } catch (e: Exception) {
                Log.e("imageError", e.message.toString())
                null
            }
        }
    }
    @Composable
    fun ImageAccountData(image: MutableState<Bitmap?>) {
// Загружаем изображение из Firebase Storage
        LaunchedEffect(Unit) {
            val bitmap = loadFirebaseImage(storageFireStore())
            image.value = bitmap
        }
    }


}