package DataClass

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val  title: String,
    val selectedIcon:ImageVector,
    val route: String

){
    object Home:BottomBarScreen(
        title = "Главная",
        selectedIcon = Icons.Default.Home,
        route = "Главная"
    ) object Volonter:BottomBarScreen(
        title = "Волонтер",
        selectedIcon = Icons.Default.Face,
        route = "Волонтер"
    ) object Market:BottomBarScreen(
        title = "Магазин",
        selectedIcon = Icons.Default.ShoppingCart,
        route = "Магазин"
    )object Profile:BottomBarScreen(
        title = "Профиль",
        selectedIcon = Icons.Default.Person,
        route = "Профиль"
    )

}
