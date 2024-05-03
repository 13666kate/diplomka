package com.example.diplom1.navigations

import DataClass.BottomBarScreen
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import screen.HomeScreenUserBlind
import screen.VolonterCardOrBlind
import viewModel.HomeScreenViewModel


@Composable
fun BottomNavGraph(navController: NavHostController,
                   homeScreenViewModel: HomeScreenViewModel){
 NavHost(navController = navController, startDestination = BottomBarScreen.Home.route){
     composable(BottomBarScreen.Home.route){
         HomeScreenUserBlind(homeViewModel =homeScreenViewModel,
             onClickCall = {
                 navController.navigate("Click"){
                     popUpTo("Click")
                 }
             } )
     }
     composable(BottomBarScreen.Volonter.route){
           VolonterCardOrBlind()
     }
     composable("Click"){
         Text(text = "Звонок ")


     }

 }

}

