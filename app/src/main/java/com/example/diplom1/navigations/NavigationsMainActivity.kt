package com.example.diplom1.navigations

import Logical.LogicalRegistrations.LogicalRegistrations
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diplom1.ScreenName.ScreenName
import com.example.diplom1.Tag.Tag
import screen.BottomBarScreen
import screen.HomeScreenUserBlind
import screen.LoadingSplashScreen
import screen.LoginScreen
import screen.RegistrationBlind
import screen.UserType
import viewModel.HomeScreenViewModel
import viewModel.LoginViewModel
import viewModel.RegistrationViewModel
import viewModel.UserType


class NavigationsMainActivity : ComponentActivity() {
    private val loginViewModel by viewModels<LoginViewModel>()
    private val registrationViewModel by viewModels<RegistrationViewModel>()
    private val homeScreenViewModel by viewModels<HomeScreenViewModel>()
    private val userType by viewModels<UserType>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = ScreenName.User) {
                composable(ScreenName.User) {
                    UserType(onclickButtonTypeBlind = {
                        try {
                            Toast(
                                context = this@NavigationsMainActivity,
                                message = "${userType.userType.value}"
                            )
                            navController.navigate(ScreenName.LoginScreen)
                        } catch (e: Exception) {
                            Log(
                                tag = Tag.navigationError,
                                message = Tag.errorNavigateNavigationsMainActivity + "${e.message}"
                            )
                        }
                    }, onclickButtonTypeVolonter = {
                        try {
                            Toast(
                                context = this@NavigationsMainActivity,
                                message = "${userType.userType.value}"
                            )
                            navcintroller(navController, ScreenName.LoginScreen)
                        } catch (e: Exception) {
                            Log(
                                tag = Tag.navigationError,
                                message = Tag.errorNavigateNavigationsMainActivity + "${e.message}"
                            )
                        }
                    }, context = this@NavigationsMainActivity,
                        userType = userType
                    )
                }
                composable(ScreenName.LoginScreen) {
                    LoginScreen(
                        loginViewModel = loginViewModel,
                        /*context = this@NavigationsMainActivity,
                        navHostController = navController,*/
                        onClickRegistrations = {
                            try {
                                navcintroller(
                                    navController = navController,
                                    nameScreen = ScreenName.RegistrationsScreen
                                )
                            } catch (e: Exception) {
                                Log(
                                    tag = Tag.navigationError,
                                    message = Tag.errorNavigateNavigationsMainActivity + "${e.message}"
                                )
                            }
                        },

                     onClickLogin = {
                        LogicalRegistrations().authentifications(
                            context = this@NavigationsMainActivity,
                            navHostController = navController,
                            userType = userType,
                            loginViewModel = loginViewModel,
                            nameNavigateHome = "Bottom"
                        )
                        })
                }

                composable(ScreenName.RegistrationsScreen) {
                    RegistrationBlind(
                        registrationViewModel = registrationViewModel,
                        context = this@NavigationsMainActivity,
                        onClickNavigate = {
                            try {
                                navcintroller(
                                    navController = navController,
                                    nameScreen = ScreenName.LoadingScreen
                                )
                                // Навигация на экран загрузки
                                // Поставить задачу в очередь с задержкой 2 секунды
                                screenLoading(
                                    navController = navController,
                                    nameNavigateScreen = ScreenName.LoginScreen
                                )

                            } catch (e: Exception) {
                                Log.e(
                                    Tag.navigationError,
                                    Tag.errorNavigateNavigationsMainActivity + "${e.message}"
                                )
                            }
                        },
                        userType = userType
                    )

                }
                composable(ScreenName.LoadingScreen) {
                    LoadingSplashScreen(registrationViewModel = registrationViewModel)
                }
                composable("Bottom") {
                   BottomBarScreen( homeScreenViewModel =homeScreenViewModel )
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

    fun Toast(
        context: Context, message: String
    ) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun Log(
        tag: String, message: String
    ) {
        Log.e(tag, message)
    }

    fun navcintroller(navController: NavController, nameScreen: String) {
        navController.navigate(nameScreen)
    }

    fun screenLoading(
        navController: NavController,
        nameNavigateScreen: String
    ) {
        Handler(Looper.getMainLooper()).postDelayed({
            navController.navigate(nameNavigateScreen) // Навигация на основной экран
        }, 4000)
    }

}


/*
typealias ComposableFunction = @Composable () -> Unit

@Composable
fun navigate(
    navController: NavController,
    nameScreen: String,

    ) {
    composable("LoginScreen") {
    }


*/











