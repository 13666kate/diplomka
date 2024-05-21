package com.example.diplom1.navigations

import DataClass.BottomBarScreen
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diplom1.ScreenName.ScreenName
import com.example.diplom1.ShedPreferences
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.uiComponets.AddVolonter
import firebase.FireBaseIDCardUser
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
import viewModel.LoginViewModel
import viewModel.ProfileViewModel
import viewModel.UserType


@Composable
fun BottomNavGraph(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel,
    cardVolonterViewModel: CardVolonterViewModel,
    bottomNavigationViewModel: BottomNavigationViewModel,
    userType: UserType,
    context: Context,
    profileViewModel: ProfileViewModel,
    navControllers: NavController,
    nameScreen: String,
) {

     bottomNavigationViewModel.statusScreen.value =
         bottomNavigationViewModel.Screen(userType = userType,context=context)
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(BottomBarScreen.Home.route) {
            HomeScreenUserBlind(homeViewModel = homeScreenViewModel,
                onClickCall = {

                    navController.navigate("Click") {
                        popUpTo("Click")
                    }
                }, cardVolonterViewModel= cardVolonterViewModel,
                context=context,
                userType = userType,
                onClickNotification = {
                   /* cardVolonterViewModel.NotificationsUserAdd(
                        cardVolonterViewModel = cardVolonterViewModel
                    )*/
                    navController.navigate(ScreenName.Notifications){
                        popUpTo(ScreenName.HomeUserBlindScreen)
                    }
                }
                )
        }
        composable(bottomNavigationViewModel.statusScreen.value) {

            val status = ShedPreferences.getShedPreferences(
                context, UserFileCollections = ShedPreferences.FileCollectionsListFriend,
                keyFile = ShedPreferences.FileListAdd
            )
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
            Text(text = "Звонок ")
        }
        composable(ScreenName.FrendListSee) {
            InformationFriendList(
                context = context,
                userType = userType,
                cardVolonterViewModel = cardVolonterViewModel,
                navController = navController,
                screenName = bottomNavigationViewModel.statusScreen.value)
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




