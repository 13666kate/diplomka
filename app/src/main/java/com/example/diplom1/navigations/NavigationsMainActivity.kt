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
import com.example.diplom1.ShedPreferences
import com.example.diplom1.Tag.Tag
import screen.BottomBarScreen
import screen.LoadingSplashScreen
import screen.LoginScreen
import screen.RegistrationBlind
import screen.RegistrationVolonters
import screen.UserType
import screen.VolonterCardOrUserBlind
import viewModel.BottomNavigationViewModel
import viewModel.CardVolonterViewModel
import viewModel.HomeScreenViewModel
import viewModel.LoginViewModel
import viewModel.ProfileViewModel
import viewModel.RegistrationViewModel
import viewModel.UserType


class NavigationsMainActivity : ComponentActivity() {
    private val loginViewModel by viewModels<LoginViewModel>()
    private val registrationViewModel by viewModels<RegistrationViewModel>()
    private val homeScreenViewModel by viewModels<HomeScreenViewModel>()
    private val userType by viewModels<UserType>()
    private val bottomNavigationViewModel by viewModels<BottomNavigationViewModel>()
    private val cardVolonterViewModel by viewModels<CardVolonterViewModel>()
    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var currentUserType = userType.getUser(this@NavigationsMainActivity)
            var currentUserTypeRegistrations = userType.getUserRegistration(this@NavigationsMainActivity)
            fun screen():String{
                if (currentUserType == ShedPreferences.statusNoAuth.value) {
                    ShedPreferences.statusNoAuth.value = "no"
                    /*ShedPreferences.saveUserType(context = this@NavigationsMainActivity,
                        ShedPreferences.statusNoAuth)*/
                    return ScreenName.User
                }else {
                    return ScreenName.BottomHome
                }
            }
            val scren = screen()


            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = scren) {

                composable(ScreenName.User) {
                    UserType(onclickButtonTypeBlind = {
                        try {

                                if (currentUserType == ShedPreferences.statusNoAuth.value) {
                                    navController.navigate(ScreenName.LoginScreen)
                                } else if (currentUserType.equals(userType.UserBlind.value)) {
                                navController.navigate(ScreenName.BottomHome)
                                    ShedPreferences.saveUserType(context = this@NavigationsMainActivity,
                                        userType = userType.UserBlind.value)

                             }

                        } catch (e: Exception) {
                            Log(
                                tag = Tag.navigationError,
                                message = Tag.errorNavigateNavigationsMainActivity + "${e.message}"
                            )
                        }
                    }, onclickButtonTypeVolonter = {
                        try {

                                if (currentUserType == ShedPreferences.statusNoAuth.value) {
                                    navController.navigate(ScreenName.LoginScreen)
                                } else if (currentUserType == userType.UserVolonters.value) {
                                    ShedPreferences.saveUserType(context = this@NavigationsMainActivity,
                                        userType = userType.UserVolonters.value)
                                    navController.navigate(ScreenName.BottomHome)
                                }

                            Toast(
                                context = this@NavigationsMainActivity,
                                message = "${currentUserType}"
                            )
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
                           // if (cardVolonterViewModel.uniqueList.isEmpty()) {
                               /* cardVolonterViewModel.getList(
                                    this@NavigationsMainActivity, cardVolonterViewModel, userType)*/
                            LogicalRegistrations().authentifications(
                                context = this@NavigationsMainActivity,
                                navHostController = navController,
                                userType = userType,
                                loginViewModel = loginViewModel,
                                nameNavigateHome = ScreenName.BottomHome,

                         ) }
                    )

                         /*   loginViewModel.Login(
                                context = this@NavigationsMainActivity,
                                navController = navController,
                                userType = userType,
                                loginViewModel = loginViewModel,
                                nameNavigateHome = ScreenName.BottomHome
                            )

                        })*/
                }
                composable(ScreenName.RegistrationsScreen) {
                    if(userType.userType.value == true ) {
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
                    }else {
                        RegistrationVolonters(
                            registrationViewModel = registrationViewModel,
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
                            context = this@NavigationsMainActivity,
                            userType = userType
                        )
                    }
                }
                composable(ScreenName.LoadingScreen) {
                    LoadingSplashScreen(registrationViewModel = registrationViewModel)
                }
                composable(ScreenName.BottomHome) {
                    BottomBarScreen(
                        homeScreenViewModel = homeScreenViewModel,
                        bottomNavigationViewModel = bottomNavigationViewModel,
                        cardVolonterViewModel = cardVolonterViewModel,
                        nameNavigate = ScreenName.User,
                        userType = userType,
                        context = this@NavigationsMainActivity,
                        navControllers = navController,
                        profileViewModel = profileViewModel

                    )


                }
                /* composable("volo") {
                     VolonterCardOrBlind(cardVolonterViewModel = cardVolonterViewModel,
                         userType = userType,
                         navController)
                 }*/

          /*      composable("Card") {
                    //  try {
                    VolonterCardOrUserBlind(cardVolonterViewModel,
                        click = {
                            navController.navigate("volo")
                        }, context = this@NavigationsMainActivity,
                        userType = userType,
                        navController = navController,
                        nameScreenAdduser = ,

                    )
*/
                    //  }catch (e:Exception){
                    //       Log.e("")
                    //   }





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
 /*   override fun onResume() {
        super.onResume()
        // Переинициализируем поток данных при каждом перезаходе пользователя

        cardVolonterViewModel.getListUserType(context = this)
    }*/
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











