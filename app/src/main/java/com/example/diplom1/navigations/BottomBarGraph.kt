package com.example.diplom1.navigations

import DataClass.BottomBarScreen
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diplom1.ScreenName.ScreenName
import com.example.diplom1.ShedPreferences
import com.example.diplom1.uiComponets.AddVolonter
import firebase.FirebaseRegistrations
import screen.CameraPreview
import screen.CameraScreen
import screen.ChatScreen
import screen.GalleryAndCameraScreen
import screen.HomeScreenUserBlind
import screen.InformationFriendList
import screen.InformationsListAdd
//import screen.ListFrendAudioCall
import screen.ListFrendChat
import screen.ListFrendVideo
import screen.ListFruends
import screen.LoginScreen
//import screen.MainScreen
import screen.NotificationsUserAdd
import screen.Profile
import screen.Sos
import screen.VidioCallScreen
import screen.VolonterCardOrBlind
import screen.VolonterCardOrUserBlind
import viewModel.BottomNavigationViewModel
import viewModel.CardVolonterViewModel
import viewModel.ChatViewModel
import viewModel.HomeScreenViewModel
import viewModel.ProfileViewModel
import viewModel.RegistrationViewModel
import viewModel.TesseractViewModel
import viewModel.UserType
import viewModel.VideoCallViewModel

//lateinit var webRTS: WebRTS

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel,
    cardVolonterViewModel: CardVolonterViewModel,
    bottomNavigationViewModel: BottomNavigationViewModel,
    registrationViewModel: RegistrationViewModel,
    chatViewModel: ChatViewModel,
    videoCallViewModel: VideoCallViewModel,
    userType: UserType,
    context: Context,
    mainActivity: NavigationsMainActivity,
    profileViewModel: ProfileViewModel,
    navControllers: NavController,
    nameScreen: String,
    tesseractViewModel: TesseractViewModel,
    lifecycleOwner: LifecycleOwner
) {

  //  webRTS = WebRTS(cardVolonterViewModel)
    if (NavigationsMainActivity().isNetworkAvailable(context)) {
        // Здесь можно выполнять инициализацию WebRTC или другие сетевые операции
        //webRTS.initializeWebRTC(context)
    } else {
        // Вывод сообщения о том, что сеть недоступна
        Log.e("Network", "Network is not available")
    }
    bottomNavigationViewModel.statusScreen.value =
        bottomNavigationViewModel.Screen(userType = userType, context = context)

    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(BottomBarScreen.Home.route) {

            HomeScreenUserBlind(
                homeViewModel = homeScreenViewModel,
                onClickCall = {
                    navController.navigate(ScreenName.Call) {
                        popUpTo(ScreenName.Call)
                    }
                },
                cardVolonterViewModel = cardVolonterViewModel,
                context = context,
                userType = userType,
                tesseractViewModel = tesseractViewModel,
                onClickNotification = {
                    navController.navigate(ScreenName.Notifications) {
                        popUpTo(ScreenName.HomeUserBlindScreen)
                    }
                },
                onClickTextRecognized = {
                    navController.navigate(ScreenName.TextRecognixed) {
                        popUpTo(ScreenName.HomeUserBlindScreen)
                    }
                },
                onClickChat = {
                    navController.navigate(ScreenName.FrendList) {
                        popUpTo(ScreenName.HomeUserBlindScreen)
                    }
                },
                onClickVideo = {
                    navController.navigate(ScreenName.ListFrendVidio)
                },
                videoCallViewModel = videoCallViewModel,
                profileViewModel = profileViewModel,
                onClickSos = {
                    navController.navigate(ScreenName.Sos)
                },
                lifecycleOwner = lifecycleOwner,

                )
        }
        composable(bottomNavigationViewModel.statusScreen.value) {

            /*  val status = ShedPreferences.getShedPreferences(
                  context, UserFileCollections = ShedPreferences.FileCollectionsListFriend,
                  keyFile = ShedPreferences.FileListAdd
              )*/
            /*       val list =  cardVolonterViewModel.FriendList(
            context,userType, cardVolonterViewModel
        )*/
            //  Toast.makeText(context,status.toString(),Toast.LENGTH_SHORT).show()
            /*  if (status == ShedPreferences.listAddNo){
       navController.navigate(ScreenName.VolonterAdd)
   } else if(status == ShedPreferences.listAddYes) {*/
            ListFruends(context,
                cardVolonterViewModel = cardVolonterViewModel,
                userType = userType,
                clickListFrend = {
                    navController.navigate(ScreenName.FrendListSee)
                },
                clickIconFriendAdd = {
                    navController.navigate(ScreenName.volonterOrUserAdd)
                }

            )
        }
        // }
        composable(BottomBarScreen.Profile.route) {
            Profile(
                context = context, userType = userType,
                navController = navControllers,
                nameScreen = nameScreen,
                profileViewModel = profileViewModel,
                cardVolonterViewModel = cardVolonterViewModel
            )
        }



        composable("Click") {
          /*  ListFrendAudioCall(
                context = context,
                userType = userType,
                cardVolonterViewModel = cardVolonterViewModel
            ) {
                navController.navigate(ScreenName.Call)
            }

           */

            //GenerateTokenScreen(context)
            //  val webRTS = remember { WebRTS() }

            /* AudioCallScrecen(cardVolonterViewModel = cardVolonterViewModel,
                 userType = userType,
                videoCallViewModel = VideoCallViewModel(),
                 context = context,
                 nameScreen = ScreenName.Call,
                 navController = navController
             )*/
        }
        composable(ScreenName.FrendListSee) {
            InformationFriendList(
                context = context,
                userType = userType,
                cardVolonterViewModel = cardVolonterViewModel,
                navController = navController,
                screenName = bottomNavigationViewModel.statusScreen.value
            )
        }
        composable(ScreenName.Sos) {
            Sos(homeScreenViewModel = homeScreenViewModel,lifecycleOwner,context
            , onBack = {
                navController.navigate(BottomBarScreen.Home.route){
                    popUpTo(BottomBarScreen.Home.route)
                }
                })
        }
        // val webRTS = WebRTS()
        composable(ScreenName.Call) {
            val callerId =
                FirebaseRegistrations().userID().toString() // Получаем идентификатор звонящего
            val calleeId = chatViewModel.saveID.value
        //    MainScreen(callerId, calleeId, userType = userType)
            /* Call(
                 cardVolonterViewModel = cardVolonterViewModel,
                 onClick = {
                     navController.navigate("Click") {
                         popUpTo("Click") { inclusive = true }
                     }
                 },
                 answer = {
                     try {
                         webRTS.answerCall()
                     } catch (e: Exception) {
                         Log.e("Call", e.message.toString())
                     }
                 },
                 reject = {
                     try {
                         webRTS.initiateCall()
                     } catch (e: Exception) {
                         Log.e("Call", e.message.toString())
                     }
                 }
             )*/
        }
        composable(ScreenName.TextRecognixed) {
            CameraScreen(context = context,
                tesseractViewModel = tesseractViewModel,
                onClickEyes = {
                    navController.navigate(ScreenName.Eyes)
                },
                onClickCameraAndGallery = {
                    navController.navigate(ScreenName.CameraAndGallery)
                },

                onBack = {
                    navController.navigate(BottomBarScreen.Home.route)
                })
        }
        composable(ScreenName.FrendList) {
            ListFrendChat(
                context = context,
                userType = userType,
                chatViewModel = chatViewModel,
                cardVolonterViewModel = cardVolonterViewModel,
                videoCallViewModel = videoCallViewModel,
                navigateToChat = {
                    navController.navigate(ScreenName.Chat)
                },
                onClickBack = {
                    navController.navigate(BottomBarScreen.Home.route) {
                        popUpTo(BottomBarScreen.Home.route)
                    }

                })
        }
        composable("m"){
            LoginScreen(navController = navController, ScreenName.VidioCall )
        }

        composable(ScreenName.FrendList) {
            ListFrendChat(
                context = context,
                userType = userType,
                chatViewModel = chatViewModel,
                cardVolonterViewModel = cardVolonterViewModel,
                videoCallViewModel = videoCallViewModel,
                navigateToChat = {
                    navController.navigate(ScreenName.Chat)
                }, onClickBack = {
                    navController.navigate(BottomBarScreen.Home.route) {
                        popUpTo(BottomBarScreen.Home.route)
                    }

                }
            )
        }
        composable(ScreenName.ListFrendVidio) {

            ListFrendVideo(
                context = context,
                userType =userType ,
                videoCallViewModel = videoCallViewModel,
                cardVolonterViewModel = cardVolonterViewModel,
               navigateToVideo = {
                    navController.navigate("m")
                },
                onClickBack = {
                    navController.navigate(BottomBarScreen.Home.route){
                        popUpTo(BottomBarScreen.Home.route)
                    }
                })
        }
        composable(ScreenName.VidioCall) {
                backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""

            VidioCallScreen(navController =navController,
                username = username,
                videoCallViewModel

                )


        }

        composable(ScreenName.Chat) {
            val status = ShedPreferences.getUserType(context)
            val chatId = if (status == userType.UserBlind.value) {
                chatViewModel.saveID.value + chatViewModel.auchID.value
            } else {
                chatViewModel.auchID.value + chatViewModel.saveID.value
            }
            ChatScreen(
                chatViewModel = chatViewModel,
                chatId = chatId,
                tesseractViewModel = tesseractViewModel,
                context = context
            )
        }

        composable(ScreenName.UserCards) {
            //    Toast.makeText(context,cardVolonterViewModel.emailStateCardUser.value,Toast.LENGTH_SHORT).show()
            VolonterCardOrBlind(
                cardVolonterViewModel = cardVolonterViewModel,
                userType = userType,
                navController = navController,
                screenName = bottomNavigationViewModel.statusScreen.value,
                "",
                context = context
            )
        }
        composable(ScreenName.VolonterAdd) {
            AddVolonter(
                navController = navController,
                nameScreen = ScreenName.volonterOrUserAdd,
                context = context,
                userType = userType
            )
        }
        composable(ScreenName.Eyes) {
            CameraPreview(
                context = context,
                tesseractViewModel = tesseractViewModel,
                onClick = {
                    navController.navigate(ScreenName.TextRecognixed)
                },
                onTextDetected = { text ->
                    tesseractViewModel.text.value = text
                    Log.d("CameraPreview", "Text detected: $text")
                }
            )
        }

        composable(ScreenName.CameraAndGallery) {
            GalleryAndCameraScreen(context = context,
                tesseractViewModel = tesseractViewModel,
                registrationViewModel = registrationViewModel,
                onBack = {
                    navController.navigate(ScreenName.TextRecognixed)

                }

            )
        }

        composable(ScreenName.volonterOrUserAdd) {
            VolonterCardOrUserBlind(
                cardVolonterViewModel = cardVolonterViewModel,
                // navController = navController , nameScreen = "volo",,
                click = {
                    try {
                        navController.navigate(ScreenName.UserCards)
                    } catch (e: Exception) {
                        Log.e("cardw", "ошибка:" + e.message.toString())
                    }

                }, context = context,
                userType = userType,
                nameScreenAdduser = ScreenName.VolonterAdd,
                navController = navController,
                clickListFrend = {
                    navController.navigate(ScreenName.UserCards)
                }
            )
        }
        composable(ScreenName.Notifications) {
            NotificationsUserAdd(cardVolonterViewModel = cardVolonterViewModel,
                context = context,
                userType = userType,

                onclickListAdd = {
                    navController.navigate(ScreenName.UserListSee)
                },
                onclickListSee = {
                    try {

                        navController.navigate(ScreenName.UserListSee)

                    } catch (e: Exception) {
                        Log.e("cardw", "ошибка:" + e.message.toString())
                    }

                })


        }
        composable(ScreenName.UserListSee) {
            InformationsListAdd(
                cardVolonterViewModel = cardVolonterViewModel,
                userType = userType,
                navController = navController,
                screenName = ScreenName.Notifications,
                context = context,
            )
        }
    }

}




