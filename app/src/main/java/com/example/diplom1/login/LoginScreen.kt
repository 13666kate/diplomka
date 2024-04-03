@file:Suppress("UNUSED_EXPRESSION")

package com.example.diplom1.login

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.diplom1.MainActivity2
import com.example.diplom1.R
import com.example.diplom1.login.mvvm.LoginViewModel
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.Olivical
import sence.kate.practica3.padding.Padding

val componets = Componets();
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel:LoginViewModel,
    onClick: () -> Unit,
//    id:Int,
//    navController: NavController
){
     val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val intent = Intent(context, MainActivity2::class.java)
     Column(
         modifier = Modifier
             .fillMaxSize()
             .background(Color(BlueBlack.value)),

         verticalArrangement = Arrangement.Center,
         horizontalAlignment = Alignment.CenterHorizontally
     ) {
        componets.Image(
            image = R.drawable.wing,
            wight = 100.dp,
            height = 100.dp,
            paddingStart = Padding.paddingZero,
            paddingTop = 20.dp,
            paddingEnd = Padding.paddingZero
            )

         componets.OutlineTextField(
             viewModel = loginViewModel,
            loginViewModel.login,
             wight = Padding.widthOutlineLoginScreen,
             height = Padding.heightOutlineLoginScreen,
             padding = Padding.paddingNormalTen,
             labelText = R.string.Login,

             )
         componets.OutlineTextField(
             viewModel =loginViewModel,
             loginViewModel.Password,
             wight = Padding.widthOutlineLoginScreen,
             height = Padding.heightOutlineLoginScreen,
             padding = Padding.paddingNormalTen,
             labelText = R.string.Password,
            )
         componets.Button(
             wight = Padding.widthButtonLoginScreen,
             height =Padding.heightButtonLoginScreen,
             buttonColor = Olivical,
             textSize = Padding.textLabelSize,
             textColor = BlueBlack,
             labelText = R.string.Vfod,
             onClick = {onClick()},
             paddingStart = Padding.paddingSmall,
             paddingTop = Padding.paddingNormalTen,
             paddingEnd = Padding.paddingSmall,

         )
         componets.TextBold(
             viewModel = loginViewModel,
             textSize = Padding.textSize ,
             labelText = R.string.registrarions,
             paddingStart = Padding.tventy,
             paddingTop =Padding.textTopRegistration ,
             paddingEnd = Padding.tventy,
         )
     }
}
