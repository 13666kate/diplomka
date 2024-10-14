package screen

import android.content.Context
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import com.example.diplom1.R
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.Red
import sence.kate.practica3.padding.Padding
import viewModel.CardVolonterViewModel
import viewModel.HomeScreenViewModel
import viewModel.UserType


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Sos(
    homeScreenViewModel: HomeScreenViewModel,
    lifecycleOwner: LifecycleOwner,
    context: Context,
    onBack: () -> Unit
) {
//    homeScreenViewModel.requestSos()
    val iconSize = 300.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Red),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackHandler {
            homeScreenViewModel.stop()
            onBack()
            homeScreenViewModel.requestSos.value = false
        }

        Box(
            modifier = Modifier
                .padding(30.dp)
                .border(
                    width = Padding.border,
                    brush = Brush
                        .linearGradient(
                            colors = listOf(Color.White, Color.White)
                        ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center,
        ) {

            val infiniteTransition = rememberInfiniteTransition()
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = ""
            )

            IconButton(
                modifier =
                Modifier
                    .size(iconSize),
                onClick = {
                },
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.gromofon),
                    contentDescription = "Add image",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                        .scale(scale),
                    tint = (Color.White),
                )
            }


        }
        Button(
            onClick = {
                homeScreenViewModel.stop()
                onBack()
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp)
                .height(70.dp)
                .border(
                    width = Padding.border,
                    brush = Brush
                        .linearGradient(
                            colors = listOf(Color.White, Color.White)
                        ),
                    shape = CircleShape
                ),
            colors = ButtonDefaults.buttonColors(
                BlueBlack,
                Color.White,
                Color.White
            )
        ) {
            androidx.compose.material3.Text(
                text = "завершить",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
            )

        }
    }
}

@Composable
fun SosVolonter(
    homeScreenViewModel: HomeScreenViewModel,
    onBack: () -> Unit,
    context: Context,
    userType: UserType,
    cardVolonterViewModel: CardVolonterViewModel,
) {

    val iconSize = 300.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Red),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackHandler {
            homeScreenViewModel.stop()
            onBack()
            homeScreenViewModel.requestSos.value = false
        }

        Box(
            modifier = Modifier
                .padding(30.dp)
                .border(
                    width = Padding.border,
                    brush = Brush
                        .linearGradient(
                            colors = listOf(Color.White, Color.White)
                        ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center,
        ) {

            val infiniteTransition = rememberInfiniteTransition()
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = ""
            )

            IconButton(
                modifier =
                Modifier
                    .size(iconSize),
                onClick = {
                },
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.gromofon),
                    contentDescription = "Add image",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                        .scale(scale),
                    tint = (Color.White),
                )
            }

        }
        Button(
            onClick = {
                homeScreenViewModel.stop()
                onBack()
                homeScreenViewModel.requestSos.value = false
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp)
                .height(70.dp)
                .border(
                    width = Padding.border,
                    brush = Brush
                        .linearGradient(
                            colors = listOf(Color.White, Color.White)
                        ),
                    shape = CircleShape
                ),
            colors = ButtonDefaults.buttonColors(
                BlueBlack,
                Color.White,
                Color.White
            )
        ) {
            androidx.compose.material3.Text(
                text = "завершить",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
            )

        }
    }
}


/*
@Composable
@Preview
fun MyScreen() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val cont = LocalContext.current
    Sos(homeScreenViewModel = HomeScreenViewModel(),
        lifecycleOwner,
        cont,
        onBack = {

        }
    )
}
*/
