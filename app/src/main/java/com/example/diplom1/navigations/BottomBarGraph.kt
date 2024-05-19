package com.example.diplom1.navigations

import DataClass.BottomBarScreen
import DataClass.UserSCardInformations
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diplom1.ScreenName.ScreenName
import com.example.diplom1.ShedPreferences
import screen.HomeScreenUserBlind
import screen.NotificationsUserAdd
import screen.Profile
import screen.VolonterCardOrBlind
import screen.VolonterCardOrUserBlind
import viewModel.BottomNavigationViewModel
import viewModel.CardVolonterViewModel
import viewModel.HomeScreenViewModel
import viewModel.LoginViewModel
import viewModel.UserType


@Composable
fun BottomNavGraph(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel,
    cardVolonterViewModel: CardVolonterViewModel,
    bottomNavigationViewModel: BottomNavigationViewModel,
    userType: UserType,
    context: Context,
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
           // cardVolonterViewModel.getList(context, cardVolonterViewModel, userType)
            VolonterCardOrUserBlind(
                cardVolonterViewModel = cardVolonterViewModel,
                // navController = navController , nameScreen = "volo",,

                click = {
                    try {
                     //   if (cardVolonterViewModel.userCards.isEmpty()) {
                    //    cardVolonterViewModel.getList(context, cardVolonterViewModel, userType)
                      //  }

                        navController.navigate(ScreenName.VolonterAdd)

                    } catch (e: Exception) {
                        Log.e("cardw", "ошибка:" + e.message.toString())
                    }

                }, context = context,
                userType = userType
            )
        }
        composable(BottomBarScreen.Profile.route) {
            val loginViewModel = LoginViewModel()
            Profile(
                context = context, userType = userType,
                navController = navControllers,
                nameScreen = nameScreen,
                loginViewModel = loginViewModel,
                cardVolonterViewModel = cardVolonterViewModel
            )
        }



        composable("Click") {
            Text(text = "Звонок ")
        }
        composable(ScreenName.VolonterAdd) {
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
        composable(ScreenName.Notifications){
         NotificationsUserAdd(cardVolonterViewModel = cardVolonterViewModel,
             context=context,
             userType = userType,
             onclickList = {

             })
             

        }

    }

}


