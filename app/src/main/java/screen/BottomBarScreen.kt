package screen

import DataClass.BottomBarScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.diplom1.navigations.BottomNavGraph
import com.example.diplom1.ui.theme.colorOlivical
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.type.ColorOrBuilder
import viewModel.HomeScreenViewModel
import androidx.compose.material.BottomNavigation
import androidx.navigation.ActivityNavigatorDestinationBuilder
import androidx.navigation.NavDestination
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.diplom1.ui.theme.BlueBlack
import sence.kate.practica3.padding.Padding

@Composable
fun BottomBarScreen(
    homeScreenViewModel: HomeScreenViewModel
) {
    val navController = rememberNavController()
    Surface(color = colorOlivical) {


        Scaffold(
            bottomBar = {
                BottomBar(navController = navController)
            }
        ) {
            it.calculateBottomPadding()
            BottomNavGraph(navController = navController, homeScreenViewModel = homeScreenViewModel)
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Volonter,
        BottomBarScreen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinations = navBackStackEntry?.destination
    BottomNavigation(
        backgroundColor = colorOlivical,
        contentColor = BlueBlack,
        elevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))

    ) {
        screens.forEach { screen ->
            AddItem(
                screens = screen,
                currentDestination = currentDestinations,
                navController = navController
            )

        }
    }
}


@Composable
fun RowScope.AddItem(
    screens: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {

    BottomNavigationItem(
        label = {
            Text(text = screens.title)
        },
        icon = {
            Icon(
                imageVector = screens.selectedIcon, contentDescription = "navigate icon "
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screens.route
        } == true,
        onClick = {
            navController.navigate(screens.route)
        })
}
