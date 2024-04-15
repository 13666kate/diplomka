package com.example.diplom1

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import screen.LoginScreen
import screen.RegistrationBlind
import viewModel.LoginViewModel
import viewModel.RegistrationViewModel


class MainActivity : ComponentActivity() {
    private val loginViewModel by viewModels<LoginViewModel>()
    private val registrationViewModel by viewModels<RegistrationViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "screenOne") {
                composable("screenOne") {

                    LoginScreen(loginViewModel = loginViewModel) {
                        try {
                            navController.navigate("screen2")
                        } catch (e: Exception) {
                            Log.e(
                                "NavigationError",
                                "Error navigating to MainActivity2: ${e.message}"
                            )
                        }
                    }
                }

                composable("screen2") {
                    RegistrationBlind(
                        registrationViewModel = registrationViewModel,
                        onClick = {

                       /* RegistrationsUsersBlind().registerUser(registrationViewModel.email.value,registrationViewModel.password.value,
                            usersData(image = registrationViewModel.imageUriState.value,
                               festName =  registrationViewModel.festName.value,
                               lastName =  registrationViewModel.lastName.value,
                               phone =  registrationViewModel.number.value,
                                email = registrationViewModel.email.value,
                                birthday = registrationViewModel.birthday.value,
                                numberIdCard = registrationViewModel.textOrRecognezedId.value,
                                identificationsPinCard = registrationViewModel.textOrRecognezedPin.value,
                                login = registrationViewModel.login.value,
                                password = registrationViewModel.password.value
                                )*/


                                  //вызвать функцию для базы
                        },


                        context = this@MainActivity
                    )


                    /*  Diplom1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                }
                }
*/
                }

            }
        }
    }

}












