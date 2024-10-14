package screen

import DataClass.BottomBarScreen
import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.diplom1.navigations.BottomNavGraph
import com.example.diplom1.navigations.NavigationsMainActivity
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.Orange
import com.example.diplom1.ui.theme.colorOlivical
import sence.kate.practica3.padding.Padding
import viewModel.BottomNavigationViewModel
import viewModel.CardVolonterViewModel
import viewModel.ChatViewModel
import viewModel.HomeScreenViewModel
import viewModel.ProfileViewModel
import viewModel.RegistrationViewModel
import viewModel.TesseractViewModel
import viewModel.UserType
import viewModel.VideoCallViewModel

@Composable
fun BottomBarScreen(
    homeScreenViewModel: HomeScreenViewModel,
    bottomNavigationViewModel: BottomNavigationViewModel,
    cardVolonterViewModel: CardVolonterViewModel,
    profileViewModel: ProfileViewModel,
    registrationViewModel: RegistrationViewModel,
    chatViewModel: ChatViewModel,
    videoCallViewModel: VideoCallViewModel,
    nameNavigate: String,
    mainActivity: NavigationsMainActivity,
    userType: UserType,
    context: Context,
    navControllers: NavController,
    tesseractViewModel: TesseractViewModel,
    lifecycleOwner: LifecycleOwner
) {
    val navController = rememberNavController()
    Surface(color = colorOlivical) {


        Scaffold(
            bottomBar = {
                BottomBar(
                    navController = navController,
                    bottomNavigationViewModel,
                    cardVolonterViewModel=cardVolonterViewModel,
                    context=context,
                    userType=userType,
                    tesseractViewModel = tesseractViewModel

                )
            }
        ) {
            it.calculateBottomPadding()
            BottomNavGraph(
                navController = navController,
                homeScreenViewModel = homeScreenViewModel,
                cardVolonterViewModel = cardVolonterViewModel,
                nameScreen = nameNavigate,
                userType = userType,
                context = context,
                navControllers = navControllers,
                bottomNavigationViewModel = bottomNavigationViewModel,
                profileViewModel = profileViewModel,
                tesseractViewModel = tesseractViewModel,
                registrationViewModel = registrationViewModel,
                chatViewModel = chatViewModel,
                videoCallViewModel = videoCallViewModel,
                mainActivity = mainActivity,
                lifecycleOwner = lifecycleOwner


            )
        }
    }
}



@Composable
fun BottomBar(
    navController: NavHostController,
    bottomNavigationViewModel: BottomNavigationViewModel,
    context: Context,
    userType: UserType,
    cardVolonterViewModel: CardVolonterViewModel,
    tesseractViewModel: TesseractViewModel,
) {
  val screenIcon =   bottomNavigationViewModel.ScreenIcon(context = context, userType = userType)
    val screens = listOf(
        BottomBarScreen.Home,
        screenIcon,
        BottomBarScreen.Profile,

        )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinations = navBackStackEntry?.destination
    BottomNavigation(
        backgroundColor = Orange,
        contentColor = BlueBlack,
        elevation = 8.dp,

        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(
                shape = RoundedCornerShape(10.dp)
            )
            .border(3.dp, color = BlueBlack, shape = RoundedCornerShape(10.dp))


    ) {
        screens.forEach { screen ->
            AddItem(
                screens = screen,
                currentDestination = currentDestinations,
                navController = navController,
                bottomNavigationViewModel,
                tesseractViewModel = tesseractViewModel

            )

        }
    }
}


@Composable
fun RowScope.AddItem(
    screens: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController,
    bottomNavigationViewModel: BottomNavigationViewModel,
    tesseractViewModel: TesseractViewModel
) {

    BottomNavigationItem(
        selectedContentColor = Color.White,
        unselectedContentColor = BlueBlack,
        modifier = Modifier.padding(top = 6.dp),
        label = {
            Text(

                text = screens.title,
                style = TextStyle(
                    fontSize = Padding.textLabelSize,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        icon = {
            Icon(
                painter = painterResource(id = screens.selectedIcon),
                contentDescription = "navigate icon ",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        tesseractViewModel.speakText(screens.title)
                    }

            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screens.route
        } == true,
        onClick = {
            navController.navigate(screens.route)
        })
}

/*@Composable
@Preview

fun MyScreen() {
    screen.BottomBarScreen(
        homeScreenViewModel = HomeScreenViewModel(),
        bottomNavigationViewModel = BottomNavigationViewModel(),
        cardVolonterViewModel = CardVolonterViewModel()
    )
}*/
