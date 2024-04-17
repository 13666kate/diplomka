package firebase

import android.content.ContentValues
import android.content.Context

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.diplom1.R
import com.example.diplom1.ui.theme.Red
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import viewModel.RegistrationViewModel

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
        registrationViewModel: RegistrationViewModel,
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val userData = mapOf(
                            "name" to name,
                            "surname" to surname,
                            "email" to email,
                            "login" to login,
                            "password" to password,
                            "image" to imageUri,
                            "pinCard" to pinCard,
                            "idCard" to idCard,
                            "birdhday" to birdhday
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
                                Log.e(ContentValues.TAG, "Ошибка : ${e.message}")
                            }
                    }
                    if (imageUri != null) {
                        val imageRef = storage.reference.child("images/${userId}/profile.jpg")
                        imageRef.putFile(imageUri)
                            .addOnSuccessListener {
                                Log.e(ContentValues.TAG, "картинка загружена")
                            }
                            .addOnFailureListener { e ->
                                Log.e(ContentValues.TAG, "Ошибка : ${e.message}")
                            }

                    } else {
                        Log.e(ContentValues.TAG, "Ошибка user ID ")
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Ошибка при регитсрации: ${task.exception?.message}",
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
                    navController.navigate(nameScreenNavigations)
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
                    } else if (error is FirebaseAuthInvalidCredentialsException) {
                        textNoRegistrations.value = R.string.ErrorPassword
                        Toast.makeText(context,  R.string.ErrorPassword, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }
}

