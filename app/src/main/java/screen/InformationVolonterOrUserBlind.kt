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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.diplom1.ShedPreferences
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.Orange
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.uiComponets.ComponetsRegistrations
import firebase.NameCollactionFirestore
import sence.kate.practica3.padding.Padding
import viewModel.CardVolonterViewModel
import viewModel.UserType

@Composable
fun VolonterCardOrBlind(
    cardVolonterViewModel: CardVolonterViewModel,
    userType: UserType,
    navController: NavHostController,
    screenName: String,
    clearSteckScreenName: String,
    context: Context,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BlueBlack),
        //  verticalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            modifier = Modifier
                .padding(top = 10.dp, start = 20.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            //    verticalArrangement = Arrangement.SpaceAround
        ) {
            ComponetsRegistrations().IconButton(
                size = 40.dp,
                icon = Icons.Default.ArrowBack, iconColor = colorOlivical
            ) {
                try {
                      navController.navigate(screenName)

                } catch (e: Exception) {
                    Log.e("card", e.message.toString())
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            //    verticalArrangement = Arrangement.SpaceAround
        ) {
            val imageSize = 150.dp
            if (cardVolonterViewModel.imageStateCardUser.value != null) {
                Image(
                    painter = rememberImagePainter(cardVolonterViewModel.imageStateCardUser.value),
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
                .height(350.dp)
                .padding(10.dp)
                .border(Padding.border, shape = RoundedCornerShape(30.dp), color = Orange)
            //  .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier

                    .fillMaxWidth()
                    .height(355.dp)
                    .padding(15.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
            cardVolonterViewModel.getDataInformationsOrCards(
                context=context,
                userType=userType,
                 cardVolonterViewModel=cardVolonterViewModel
            )
                Text(
                    text = cardVolonterViewModel.nameStateCardUser,
                    informations = "Имя"
                )
                Text(
                    text = cardVolonterViewModel.surnameStateCardUser,
                    informations = "Фамилия"
                )
                Text(
                    text = cardVolonterViewModel.emailStateCardUser,
                    informations = "Email"
                )
                Text(
                    text = cardVolonterViewModel.numberUserCarsd,
                    informations = "Телефон"
                )
                Text(
                    text = cardVolonterViewModel.regionStateCardUser,
                    informations = "Область проживания"
                )
                Text(
                    text = cardVolonterViewModel.rayonStateCardUser,
                    informations = "Район проживания"
                )
                Text(
                    text = cardVolonterViewModel.adresStateCardUser,
                    informations = "Адрес"
                )


                val currentUserType = ShedPreferences.getUserType(context)
                if (currentUserType != null) {
                    if (currentUserType == userType.UserVolonters.value) {
                        Text(
                            text = userType.UserBlind,
                            informations = "Статус"
                        )
                    }
                    if (currentUserType == userType.UserBlind.value) {
                        Text(
                            text = userType.UserVolonters,
                            informations = "Статус"
                        )
                        Text(
                            text = cardVolonterViewModel.expVolonters,
                            informations = "Годы в волонтерстве"
                        )
                        Text(
                            text = cardVolonterViewModel.aboutmeUserCarsd,
                            informations = "О себе"
                        )
                    }
                }
            }

        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 110.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            modifier = Modifier
                .width(200.dp)
                .height(70.dp),
            colors = ButtonDefaults.buttonColors(colorOlivical),
            onClick = {
                //имя коллекции в зввисимости от статуса
                cardVolonterViewModel.nameCollections(
                    context = context,
                    userType=userType)
                    //оздание запроса
                cardVolonterViewModel.requesAddUser(
                    email = cardVolonterViewModel.emailStateCardUser.value,
                    nameCollection = cardVolonterViewModel.nameCollection.value,
                    stateIdAuch = cardVolonterViewModel.searchOrEmailUser,
                    stateEmailUserSerch = cardVolonterViewModel.stateIdUserSerch,
                    context = context,
                    userType = userType
                )

            }

        ) {
            Text(
                text = "Добавить",
                style = TextStyle(
                    color = BlueBlack,
                    fontSize = 25.sp,
                    // textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                )

            )
        }
    }
}


@Composable
fun Text(
    text: MutableState<String>,
    informations: String
) {
    val textBottomPadding = 18.dp
    val fontSize = 20.sp
    Text(
        modifier = Modifier
            .padding(bottom = textBottomPadding),
        text = "$informations:  " + " ${text.value}",
        style = TextStyle(
            color = colorOlivical,
            fontSize = fontSize,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold,
        )

    )
}
/*

@Composable
@Preview
fun MyScreen() {
   // val navHostController:NavHostController  =
    VolonterCardOrBlind(cardVolonterViewModel = CardVolonterViewModel(),UserType(),"","")
}
*/
