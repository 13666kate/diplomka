package screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.diplom1.ShedPreferences
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.Orange
import com.example.diplom1.ui.theme.Red
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.uiComponets.ComponetsRegistrations
import firebase.FireBaseIDCardUser
import firebase.FirebaseProfile
import kotlinx.coroutines.flow.first
import sence.kate.practica3.padding.Padding
import viewModel.CardVolonterViewModel
import viewModel.LoginViewModel
import viewModel.ProfileViewModel
import viewModel.UserType

@Composable
fun Profile(
    context: Context,
    userType: UserType,
    navController: NavController,
    profileViewModel: ProfileViewModel,
    cardVolonterViewModel: CardVolonterViewModel,
    nameScreen: String
) {

    profileViewModel.getData(
        userType, context, cardVolonterViewModel
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BlueBlack),
    //     verticalArrangement = Arrangement.SpaceAround
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            //    verticalArrangement = Arrangement.SpaceAround
        ) {
            val imageSize = 150.dp
            if (profileViewModel.bitmap.value != null) {
                Image(
                    painter = rememberImagePainter(profileViewModel.bitmap.value),
                    contentDescription = "ina",
                    modifier = Modifier
                        .size(imageSize)
                        .clip(shape = CircleShape)
                        .border(
                            width = Padding.border,
                            brush = Brush.linearGradient(
                                colors = listOf(colorOlivical, Orange)
                            ), shape = CircleShape
                        ),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(cardVolonterViewModel.imageAvatarValueNo.value),
                    contentDescription = "ina",
                    modifier = Modifier
                        .size(imageSize)
                        .clip(shape = CircleShape)
                        .border(
                            width = Padding.border,
                            brush = Brush.linearGradient(
                                colors = listOf(colorOlivical, Orange)
                            ), shape = CircleShape
                        ),
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(455.dp)
                .padding(10.dp)
              .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .height(400.dp)
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp)
                    .verticalScroll(rememberScrollState()),
            ) {

                Text(
                    text = profileViewModel.nameUser,
                    informations = "Имя"
                )
                Text(
                    text = profileViewModel.surnameUser,
                    informations = "Фамилия"
                )
                Text(
                    text = profileViewModel.birhday,
                    informations = "День рождение"
                )
                Text(
                    text = profileViewModel.email,
                    informations = "Email"
                )
                Text(
                    text = profileViewModel.phone,
                    informations = "Телефон"
                )
                Text(
                    text = profileViewModel.region,
                    informations = "Область проживания"
                )
                Text(
                    text = profileViewModel.rayon,
                    informations = "Район проживания"
                )
                Text(
                    text = profileViewModel.address,
                    informations = "Адрес"
                )


                val currentUserType = ShedPreferences.getUserType(context)
                if (currentUserType != null) {
                    if (currentUserType == userType.UserVolonters.value) {

                        Text(
                            text = userType.UserVolonters,
                            informations = "Статус"
                        )
                        Text(
                            text = profileViewModel.yersVolonters,
                            informations = "Годы в волонтерстве"
                        )
                        Text(
                            text = profileViewModel.yersVolonters,
                            informations = "О себе"
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                //    .padding(bottom = 100.dp),
                    , horizontalAlignment = Alignment.End
              //  verticalArrangement = Arrangement.Center
            ) {
                ComponetsRegistrations().IconButton(
                    size = 70.dp, icon = Icons.Default.ExitToApp,
                    iconColor = Red,
                    onClick = {

                        // Очищаем текущий список пользователей
                        //  cardVolonterViewModel.uniqueList=user.value.toMutableList()
                        userType.user(
                            context,
                            navController,
                            nameScreen,
                            cardVolonterViewModel,
                            userType
                        )
                        // Получаем новый список пользователей

                        // Вызываем ваш метод обновления пользовательского интерфейса
                        //     cardVolonterViewModel.listUser.addAll(user)

                        // Сохраняем статус пользователя
                        ShedPreferences.saveUserType(
                            context = context,
                            userType = ShedPreferences.statusNoAuth.value
                        )


                    }
                )


            }

        }
    }
}


/*
@Composable
@Preview
fun MyScreen(context: Context) {

    Profile(context = context, UserType())
}*/
