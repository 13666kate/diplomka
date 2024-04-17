package com.example.diplom1.navigations

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diplom1.R
import com.google.firebase.auth.FirebaseAuth
import firebase.FirebaseRegistrations
import screen.HomeScreenUserBlind
import screen.LoadingSplashScreen

import screen.LoginScreen
import screen.RegistrationBlind
import screen.UserType
import viewModel.LoginViewModel
import viewModel.RegistrationViewModel


class NavigationsMainActivity : ComponentActivity() {
    private val loginViewModel by viewModels<LoginViewModel>()
    private val registrationViewModel by viewModels<RegistrationViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "User") {
                composable("User") {
                    UserType(onclickButtonTypeBlind = {
                        try {
                            navController.navigate("LoginScreen")
                        } catch (e: Exception) {
                            Log.e(
                                "NavigationError",
                                "Error navigating to MainActivity2: ${e.message}"
                            )
                        }
                    }) {
                        try {
                            navController.navigate("User")
                        } catch (e: Exception) {
                            Log.e(
                                "NavigationError",
                                "Error navigating to MainActivity2: ${e.message}"
                            )
                        }

                    }
                }
                composable("LoginScreen") {
                    LoginScreen(loginViewModel = loginViewModel,
                        onClickLogin = {
                            try {

                            FirebaseRegistrations().userAuthentication(
                                    email = loginViewModel.login.value,
                                    password = loginViewModel.Password.value,
                                    navController = navController,
                                    textNoRegistrations = loginViewModel.textAuthenticationsValue,
                                    collectionsFireStore = "usersBlind",
                                    colorOutline = loginViewModel.ColorOtline,
                                    context = this@NavigationsMainActivity,
                                    nameScreenNavigations = "homeUserBlindScreen"
                                )


                            } catch (e: Exception) {
                                Log.e(
                                    "NavigationError",
                                    "Error navigating to MainActivity2: ${e.message}"
                                )
                            }

                        },
                        onClickRegistrations = {
                            try {
                                navController.navigate("registrationsScreen")
                            } catch (e: Exception) {
                                Log.e(
                                    TAG,
                                    "Error navigating to MainActivity2: ${e.message}"
                                )
                            }
                        })
                }

                composable("registrationsScreen") {
                    RegistrationBlind(
                        registrationViewModel = registrationViewModel,
                        context = this@NavigationsMainActivity,
                        onClickNavigate = {
                            try {
                                navController.navigate("loadingScreen") // Навигация на экран загрузки
                                // Поставить задачу в очередь с задержкой 2 секунды
                                Handler(Looper.getMainLooper()).postDelayed({
                                    navController.navigate("LoginScreen") // Навигация на основной экран
                                }, 4000)
                            } catch (e: Exception) {
                                Log.e(
                                    "NavigationError",
                                    "Error navigating to MainActivity2: ${e.message}"
                                )
                            }
                        },
                    )

                }
                composable("loadingScreen") {
                    LoadingSplashScreen(registrationViewModel = registrationViewModel)
                }


}

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



/*typealias ComposableFunction = @Composable () -> Unit
    @Composable
    fun navigations(
        nameNavigations: String,
        composableFunction: (NavController) -> Unit
    ) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "User") {
            composable(nameNavigations) {
                composableFunction(navController)
            }
        }
    }
}*/












