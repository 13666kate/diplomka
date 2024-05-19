package DataClass

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.example.diplom1.R

sealed class BottomBarScreen(
    val  title: String,
    val selectedIcon:Int,
    val route: String

){
    object Home:BottomBarScreen(
        title = "Главная",
        selectedIcon =  R.drawable.baseline_home_24,
        route = "Главная"
    ) object Volonter:BottomBarScreen(
        title = "Волонтер",
        selectedIcon = R.drawable.volonter,
        route = "Волонтер"
    ) object Market:BottomBarScreen(
        title = "Магазин",
        selectedIcon = R.drawable.baseline_shopping_cart_24,
        route = "Магазин"
    )object Profile:BottomBarScreen(
        title = "Профиль",
        selectedIcon = R.drawable.baseline_person_24,
        route = "Профиль"
    )object User:BottomBarScreen(
        title = "Подопечный",
        selectedIcon = R.drawable.volonter,
        route = "Подопечный"
    )

}
