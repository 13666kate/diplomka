package screen


import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.diplom1.ShedPreferences
import com.example.diplom1.ui.theme.Black
import com.example.diplom1.uiComponets.IDCardComponets
import kotlinx.coroutines.flow.MutableStateFlow
import viewModel.UserType
import java.nio.file.WatchEvent

@Composable
fun VolonterCardOrUserBlind(
    cardVolonterViewModel: CardVolonterViewModel,
//    navController: NavHostController,
    navController: NavHostController,
    nameScreenAdduser: String,
    context: Context,
    userType: UserType,
    click: () -> Unit,
    clickListFrend: () -> Unit
) {
    val statusList = ShedPreferences.getShedPreferences(
        context = context,
        UserFileCollections = ShedPreferences.FileCollectionsListFriend,
        keyFile = ShedPreferences.FileListAdd
    )
    //   if (statusList == ShedPreferences.listAddNo) {
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
                context = context,
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
//}

@Composable
fun ListFruends(
    context: Context,
    cardVolonterViewModel: CardVolonterViewModel,
    userType: UserType,
    clickListFrend: () -> Unit,
    clickIconFriendAdd: () -> Unit,
) {
    val idCardComponets = IDCardComponets()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BlueBlack)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val size = 55.dp
        val iconColor = colorOlivical
        val textStete = remember {
            mutableStateOf("")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val status = ShedPreferences.getUserType(context)
            if (status == userType.UserBlind.value) {
                textStete.value = "Ваш список Волонтеров"
            } else if (status == userType.UserVolonters.value) {
                textStete.value = "Ваш список Пользователей"
            } else {
                textStete.value = "Cписок"
            }
            Row(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 15.dp),
                // .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = textStete.value,
                    style = TextStyle(
                        color = colorOlivical,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
            IconButton(
                modifier =
                Modifier.size(size),
                onClick = {
                    clickIconFriendAdd()
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_group_add_24),
                    contentDescription = "Add image",
                    modifier = Modifier
                        .size(size),
                    tint = (iconColor),
                )
            }
        }
        idCardComponets.LazyColumnList(context = context,
            cardVolonterViewModel = cardVolonterViewModel,
            userType = userType,
            onClick = {
                clickListFrend()
            })
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

@Preview
@Composable
fun CardVolonter() {
    val context = LocalContext.current
    val viewModel = CardVolonterViewModel() // Здесь вы можете создать заглушечный ViewModel
    val userType = UserType()
    ListFruends(
        context = context,
        cardVolonterViewModel = CardVolonterViewModel(),
        userType = UserType(),
        {}
    ) {

    }
}
