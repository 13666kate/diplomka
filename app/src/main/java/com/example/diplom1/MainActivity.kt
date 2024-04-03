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
import screen.LoginScreen
import screen.RegistrationBlind
import viewModel.LoginViewModel
import viewModel.RegistrationViewModel


class MainActivity : ComponentActivity() {
    private val loginViewModel by viewModels<LoginViewModel>()
    private val registrationViewModel by viewModels<RegistrationViewModel>()
    lateinit var context: Context

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
                            )}}
                    }
                composable("screen2") {
                    RegistrationBlind(
                        registrationViewModel = registrationViewModel,
                        onClick = {

                        },
                        context = this@MainActivity)


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
    }




