package com.example.diplom1.navigations

import Logical.LogicalRegistrations.LogicalRegistrations
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diplom1.ScreenName.ScreenName
import com.example.diplom1.ShedPreferences
import com.example.diplom1.Tag.Tag
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.firestore.FirebaseFirestore
import screen.BottomBarScreen
import screen.LoadingSplashScreen
import screen.LoginScreen
import screen.RegistrationBlind
import screen.RegistrationVolonters
import screen.SosVolonter
import screen.UserType
import viewModel.BottomNavigationViewModel
import viewModel.CardVolonterViewModel
import viewModel.ChatViewModel
import viewModel.HomeScreenViewModel
import viewModel.LoginViewModel
import viewModel.ProfileViewModel
import viewModel.RegistrationViewModel
import viewModel.TesseractViewModel
import viewModel.UserType
import viewModel.VideoCallViewModel


class NavigationsMainActivity : ComponentActivity() {
    private val loginViewModel by viewModels<LoginViewModel>()
    private val registrationViewModel by viewModels<RegistrationViewModel>()
    private val homeScreenViewModel by viewModels<HomeScreenViewModel>()
    private val userType by viewModels<UserType>()
    private val bottomNavigationViewModel by viewModels<BottomNavigationViewModel>()
    private val cardVolonterViewModel by viewModels<CardVolonterViewModel>()
    private val tesseractViewModel by viewModels<TesseractViewModel>()
    private val profileViewModel by viewModels<ProfileViewModel>()
    private val videoCallViewModel by viewModels<VideoCallViewModel>()
    private val chatViewModel by viewModels<ChatViewModel>()
    private lateinit var videoCallViewModels: VideoCallViewModel
    private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allPermissionsGranted = permissions.entries.all { it.value }
            if (!allPermissionsGranted) {
                Toast.makeText(this, "Permissions are required for video call", Toast.LENGTH_SHORT).show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*if(videoCallViewModel.isPermissionGranted(this@NavigationsMainActivity)){
           videoCallViewModel.askPermissions(this)
        }*/
       /* videoCallViewModels = ViewModelProvider(this).get(VideoCallViewModel::class.java)

        val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions.all { it.value }) {
                    setupVideoCall()
                } else {
                    Toast.makeText(this, "Permissions required for video call", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        if (videoCallViewModels.isPermissionGranted(this)) {
            setupVideoCall()
        } else {
            permissionLauncher.launch(videoCallViewModels.permissions)
        }*/


        FirebaseApp.initializeApp(this)
        // Инициализация Firebase App Check
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
        setContent {
            checkAndRequestPermissions()
            /*if (!videoCallViewModel.isPermissionGranted(this)) {
                videoCallViewModel.askPermissions(this)
            }*/

            var currentUserType = userType.getUser(this@NavigationsMainActivity)
            var currentUserTypeRegistrations =
                userType.getUserRegistration(this@NavigationsMainActivity)

            fun screen(): String {
                if (currentUserType == ShedPreferences.statusNoAuth.value) {
                    ShedPreferences.statusNoAuth.value = "no"
                    /*ShedPreferences.saveUserType(context = this@NavigationsMainActivity,
                        ShedPreferences.statusNoAuth)*/
                    return ScreenName.User
                } else {
                    return ScreenName.BottomHome
                }
            }

            val scren = screen()


            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = scren) {

                composable(ScreenName.User) {
                    LaunchedEffect(this@NavigationsMainActivity) {
                        tesseractViewModel.initializeTTS(this@NavigationsMainActivity)
                        tesseractViewModel.handleLifecycle(this@NavigationsMainActivity,
                            this@NavigationsMainActivity)

                    }
                    UserType(tesseractViewModel= tesseractViewModel,
                        lifecycleOwner = this@NavigationsMainActivity,
                        onclickButtonTypeBlind = {
                          //  tesseractViewModel.speakText("Пользователь")
                        try {

                            if (currentUserType == ShedPreferences.statusNoAuth.value) {
                                navController.navigate(ScreenName.LoginScreen)
                            } else if (currentUserType.equals(userType.UserBlind.value)) {
                                navController.navigate(ScreenName.BottomHome)
                                ShedPreferences.saveUserType(
                                    context = this@NavigationsMainActivity,
                                    userType = userType.UserBlind.value
                                )

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
                                ShedPreferences.saveUserType(
                                    context = this@NavigationsMainActivity,
                                    userType = userType.UserVolonters.value
                                )
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
                        tesseractViewModel=tesseractViewModel,
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

                                )
                        }
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
                    if (userType.userType.value == true) {
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
                            userType = userType,
                            tesseractViewModel = tesseractViewModel
                        )
                    } else {
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
                    val status = ShedPreferences.getUserType(this@NavigationsMainActivity)
                    Log.e("sos", homeScreenViewModel.requestSos.value.toString())
                    if (homeScreenViewModel.requestSos.value == true) {
                        if (status == userType.UserVolonters.value)

                            LaunchedEffect(Unit) {

                                homeScreenViewModel.responseVolonters(
                                    context = this@NavigationsMainActivity,
                                    userType = userType,
                                    cardVolonterViewModel = cardVolonterViewModel,
                                    onclick = {
                                        navcintroller(
                                            navController = navController,
                                            ScreenName.SosVolo
                                        )
                                    },
                                )
                            }
                    }
                    val requestPermissionsLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestMultiplePermissions()
                    ) { permissions ->
                        val permissionsGranted = permissions[Manifest.permission.CAMERA] == true &&
                                permissions[Manifest.permission.RECORD_AUDIO] == true
                        videoCallViewModel.handlePermissions(
                            this@NavigationsMainActivity,
                            permissionsGranted
                        )
                    }

                    LaunchedEffect(Unit) {
                        val permissionsGranted = ContextCompat.checkSelfPermission(
                            this@NavigationsMainActivity,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(
                                    this@NavigationsMainActivity,
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED
                        if (!permissionsGranted) {
                            requestPermissionsLauncher.launch(
                                arrayOf(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.RECORD_AUDIO
                                )
                            )
                        } else {
                            videoCallViewModel.handlePermissions(
                                this@NavigationsMainActivity,
                                permissionsGranted
                            )
                        }
                    }
                    val f = remember {
                        mutableStateOf("")
                    }
                    if (status == userType.UserBlind.value) {
                        FirebaseFirestore.getInstance().collection(ScreenName.Sos)
                       val serch =  cardVolonterViewModel.idByEmailSearch(
                            stringSearch = "requestStatusSos",
                            context = this@NavigationsMainActivity,
                            userType = userType,
                            f,
                            nameFileInCollectionSearch = "requestsos"

                        )
                    if (homeScreenViewModel._isBlockingScreenVisible.value == true) {
                        if (f.value != null) {

                            SosVolonter(
                                homeScreenViewModel = homeScreenViewModel,
                                onBack = {
                                    navController.navigate(ScreenName.BottomHome)
                                },
                                context = this@NavigationsMainActivity,
                                userType = userType,
                                cardVolonterViewModel = cardVolonterViewModel
                            )
                        }
                    }
                    }
                    if (homeScreenViewModel._isBlockingScreenVisible.value == false) {
                        BottomBarScreen(
                            homeScreenViewModel = homeScreenViewModel,
                            bottomNavigationViewModel = bottomNavigationViewModel,
                            cardVolonterViewModel = cardVolonterViewModel,
                            nameNavigate = ScreenName.User,
                            userType = userType,
                            context = this@NavigationsMainActivity,
                            navControllers = navController,
                            profileViewModel = profileViewModel,
                            tesseractViewModel = tesseractViewModel,
                            registrationViewModel = registrationViewModel,
                            videoCallViewModel = videoCallViewModel,
                            chatViewModel = chatViewModel,
                            mainActivity = this@NavigationsMainActivity,
                            lifecycleOwner = this@NavigationsMainActivity

                        )

                    }
                }


                composable(ScreenName.SosVolo) {
                    SosVolonter(
                        homeScreenViewModel = homeScreenViewModel,
                        onBack = {
                            navController.navigate(ScreenName.BottomHome)
                        },
                        context = this@NavigationsMainActivity,
                        userType = userType,
                        cardVolonterViewModel = cardVolonterViewModel
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

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

   /* fun onPeerConnected() {
        videoCallViewModels.isPeerConnected.value = true
    }*/

/*    private fun setupVideoCall() {
        setContent {
         VideoCall(videoCallViewModel = videoCallViewModels, mainActivity = this,
             cardVolonterViewModel = cardVolonterViewModel)
        }*/

    fun onPeerConnected() {
        videoCallViewModel.isPeerConnected.value = true
    }
    private fun checkAndRequestPermissions() {
        if (!isPermissionGranted()) {
            requestPermissionLauncher.launch(permissions)
        }
    }

    private fun isPermissionGranted(): Boolean {
        return permissions.all {
            ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}





 /*   override fun onResume() {
        super.onResume()
        // Переинициализируем поток данных при каждом перезаходе пользователя

        cardVolonterViewModel.getListUserType(context = this)
    }*/



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











