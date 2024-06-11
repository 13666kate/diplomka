package com.example.diplom1.navigations

import DataClass.BottomBarScreen
import WebRTS
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diplom1.ScreenName.ScreenName
import com.example.diplom1.uiComponets.AddVolonter
import screen.CameraPreview
import screen.CameraScreen
import screen.GalleryAndCameraScreen
import screen.HomeScreenUserBlind
import screen.InformationFriendList
import screen.InformationsListAdd
import screen.ListFruends
import screen.NotificationsUserAdd
import screen.Profile
import screen.VolonterCardOrBlind
import screen.VolonterCardOrUserBlind
import viewModel.BottomNavigationViewModel
import viewModel.CardVolonterViewModel
import viewModel.HomeScreenViewModel
import viewModel.ProfileViewModel
import viewModel.RegistrationViewModel
import viewModel.TesseractViewModel
import viewModel.UserType

lateinit var webRTS: WebRTS
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel,
    cardVolonterViewModel: CardVolonterViewModel,
    bottomNavigationViewModel: BottomNavigationViewModel,
    registrationViewModel: RegistrationViewModel,
    userType: UserType,
    context: Context,
    profileViewModel: ProfileViewModel,
    navControllers: NavController,
    nameScreen: String,
    tesseractViewModel: TesseractViewModel
) {

      webRTS = WebRTS(cardVolonterViewModel)
    if (NavigationsMainActivity().isNetworkAvailable(context)) {
        // Здесь можно выполнять инициализацию WebRTC или другие сетевые операции
        webRTS.initializeWebRTC(context)
    } else {
        // Вывод сообщения о том, что сеть недоступна
        Log.e("Network", "Network is not available")
    }
     bottomNavigationViewModel.statusScreen.value =
         bottomNavigationViewModel.Screen(userType = userType,context=context)

    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(BottomBarScreen.Home.route) {
           /* val webRTS = WebRTS()
            webRTS.initializeWebRTC(context)*/
            HomeScreenUserBlind(homeViewModel = homeScreenViewModel,
                onClickCall = {

                    navController.navigate("Click") {
                        popUpTo("Click")
                    }
                }, cardVolonterViewModel= cardVolonterViewModel,
                context=context,
                userType = userType,
                onClickNotification = {
                    navController.navigate(ScreenName.Notifications){
                        popUpTo(ScreenName.HomeUserBlindScreen)
                    }
                },
                onClickTextRecognized = {
                    navController.navigate(ScreenName.TextRecognixed){
                        popUpTo(ScreenName.HomeUserBlindScreen)
                    }
                }
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
            //GenerateTokenScreen(context)
          //  val webRTS = remember { WebRTS() }

           /* AudioCallScreen(cardVolonterViewModel = cardVolonterViewModel,
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
                screenName = bottomNavigationViewModel.statusScreen.value)
        }
       // val webRTS = WebRTS()
        composable(ScreenName.Call) {
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
           CameraScreen(context =context ,
                tesseractViewModel =tesseractViewModel,
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


            composable(ScreenName.UserCards) {
        //    Toast.makeText(context,cardVolonterViewModel.emailStateCardUser.value,Toast.LENGTH_SHORT).show()
            VolonterCardOrBlind(
                cardVolonterViewModel = cardVolonterViewModel,
                userType = userType,
                navController = navController,
                screenName =  bottomNavigationViewModel.statusScreen.value,
               "",
                context = context
            )
        }
        composable(ScreenName.VolonterAdd){
            AddVolonter(
                navController =navController ,
                nameScreen = ScreenName.volonterOrUserAdd,
                context=context,
                userType=userType
            )
        }
     composable(ScreenName.Eyes){
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

        composable(ScreenName.CameraAndGallery){
           GalleryAndCameraScreen(context = context,
               tesseractViewModel = tesseractViewModel,
               registrationViewModel = registrationViewModel,
               onBack = {
                   navController.navigate(ScreenName.TextRecognixed)

               }

           )
        }

        composable(ScreenName.volonterOrUserAdd){
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
        composable(ScreenName.Notifications){
         NotificationsUserAdd(cardVolonterViewModel = cardVolonterViewModel,
             context=context,
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
                userType =userType ,
                navController = navController,
                screenName = ScreenName.Notifications,
                context =context,
            )
        }
        }

    }




