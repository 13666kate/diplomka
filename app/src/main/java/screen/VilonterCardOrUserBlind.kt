package screen


import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diplom1.R
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.uiComponets.ComponetsRegistrations
import sence.kate.practica3.padding.Padding
import viewModel.CardVolonterViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.example.diplom1.ui.theme.Black
import com.example.diplom1.uiComponets.IDCardComponets
import kotlinx.coroutines.flow.MutableStateFlow
import viewModel.UserType

@Composable
fun VolonterCardOrUserBlind(
    cardVolonterViewModel: CardVolonterViewModel,
//    navController: NavHostController,
    //navController:  NavHostController,
 //   nameScreen: String,
    context: Context,
    userType: UserType,
    click:()->Unit
) {
    val idCardComponets = IDCardComponets()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BlueBlack)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Search(
           textValeu = cardVolonterViewModel.textSearch,
            text = cardVolonterViewModel.text,
            textColor = BlueBlack,
            textLabel = R.string.searchVolonter,
            icon = Icons.Default.Search,
            iconSize = 40.dp,
            textSize = 17.sp,
            iconColor = BlueBlack
        ) {

        }
    //   cardVolonterViewModel.loadDataIfNeeded()
      //  val cardVolonterViewModel: CardVolonterViewModel = rememberViewModel()
        if (cardVolonterViewModel.text.value.isNotEmpty()) {
            idCardComponets.LazyColumnSearch(
                cardVolonterViewModel = cardVolonterViewModel,
                click = click,
                context =context,
                userType = userType
            )
        } else {

            idCardComponets.LazyColumnUsers(
                cardVolonterViewModel = cardVolonterViewModel,
                click = click,
                context = context,
                userType = userType
            )
        }

       // screen.Text(text =cardVolonterViewModel.searchOrEmailUser , informations = "польз")
     //   screen.Text(text =cardVolonterViewModel.stateIdUserSerch , informations = "вол")
        //screen.Text(text =cardVolonterViewModel.stateIdUserSerch2 , informations = "воло")

      //  screen.Text(text =cardVolonterViewModel.stateEmailUserSerch.value , informations = "k")
    }

}






@Composable
fun Row(
    baground: Color,
    cardVolonterViewModel: CardVolonterViewModel,

    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .background(color = baground, shape = CircleShape)
    )
    {

        //  val id = cardVolonterViewModel.uidByEmailSeaech()
        //  FirebaseRegistrations().ImageAccountData(
        //     image = cardVolonterViewModel.imageState,
        //      path = FireBaseIDCardUser().storageFireStore(userId = id)
        //  )

        if (cardVolonterViewModel.imageState.value != null) {
            val bitmap: Bitmap = cardVolonterViewModel.imageState.value!!

            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "imageStore",
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = CircleShape)
                    .border(
                        width = Padding.border,
                        brush = Brush.linearGradient(
                            colors = listOf(Black, Black)
                        ), shape = CircleShape
                    )
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,

                )
        } else {
            Image(
                imageVector = cardVolonterViewModel.imageStateAccountAvatar.value,//viewModel.imageStateAccountAvatar.value,
                contentDescription = "imageStore",
                modifier = Modifier
                    .size(100.dp)
                    .border(
                        width = Padding.border,
                        brush = Brush.linearGradient(
                            colors = listOf(Black, Black)
                        ), shape = CircleShape
                    ),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(BlueBlack)
            )
        }


    }
}



@Composable
fun Search(
    textValeu: MutableStateFlow<String>,
    text: MutableState<String>,
    textColor: Color,
    textLabel: Int,
    icon: ImageVector,
    iconColor: Color,
    iconSize: Dp,
    textSize: TextUnit,
    onClickIcon: () -> Unit,
) {

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        value = text.value,
        onValueChange = { value ->
            text.value = value
            textValeu.value = value
        }, label = {
            Text(
                text = stringResource(id = textLabel),
                modifier = Modifier, style = TextStyle(
                    color = textColor,
                    fontSize = textSize,
                    fontWeight = FontWeight.Bold,
                )
            )
        },
        trailingIcon = {

            ComponetsRegistrations().IconButton(
                size = iconSize,
                icon = icon,
                iconColor = iconColor,
                onClick = {
                    onClickIcon()
                }
            )


        },
        shape = CircleShape,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = colorOlivical,
            focusedBorderColor = BlueBlack,
            unfocusedBorderColor = BlueBlack
        )


    )
}

/*
@Preview
@Composable
fun CardVolonter() {
    //   VolonterCardOrUserBlind(CardVolonterViewModel())
    screen.Row(baground = colorOlivical, CardVolonterViewModel())
}*/
