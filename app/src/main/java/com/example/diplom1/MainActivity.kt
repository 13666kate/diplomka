package com.example.diplom1

import android.content.Context
import android.content.Intent
import android.content.res.XmlResourceParser
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diplom1.login.LoginScreen
import com.example.diplom1.login.mvvm.LoginViewModel
import com.example.diplom1.registration.mvvm.RegistrationViewModel
import com.example.diplom1.ui.theme.Diplom1Theme


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
                    val xmlResId = R.layout.activity_main
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { context ->
                            try {
                                LayoutInflater.from(context).inflate(xmlResId, null)
                            } catch (e: Exception) {
                                // Обработка ошибки загрузки ресурсов изображений
                                Log.e("ImageLoadingError", "Failed to load image resources: ${e.message}")
                                // Вернуть пустой вид, если возникла ошибка загрузки ресурсов изображений
                                FrameLayout(context)
                            }
                        }
                    )
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




