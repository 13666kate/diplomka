package com.example.diplom1.uiComponets

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diplom1.R
import com.example.diplom1.ui.theme.Black
import com.example.diplom1.ui.theme.BlackWhite
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.Orange
import com.example.diplom1.ui.theme.colorOlivical
import firebase.FirebaseRegistrations
import screen.componetsHome
import sence.kate.practica3.padding.Padding
import viewModel.HomeScreenViewModel

class ComponetsHome {

    @Composable
    fun Text(
        paddingBottom: Dp,
        color: Color,
        text: String,
        paddingTop: Dp,
        paddingStart: Dp,
        fontSize: TextUnit,
        fontWeight: FontWeight,
    ) {
        androidx.compose.material3.Text(
            modifier = Modifier
                .padding(
                    top = paddingTop,
                    bottom = paddingBottom,
                    start = paddingStart

                ),
            text = text,
            color = color,
            style = TextStyle(
                fontSize = fontSize,
                fontWeight = fontWeight,
                // fontStyle = (fontFamily =R.font.osnova )
            )
        )
    }

    @Composable
    fun ImageVar(
        viewModel: HomeScreenViewModel,
        size: Dp,

        ) {
        Box(
            modifier = Modifier
                .size(size = size)
                .clip(shape = CircleShape)
            //  .padding(start = 8.dp)

        ) {

            FirebaseRegistrations().ImageAccountData(image = viewModel.imageState,
                path = FirebaseRegistrations().storageFireStore())
            if (viewModel.imageState.value != null) {
                val bitmap: Bitmap = viewModel.imageState.value!!
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "imageStore",
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = Padding.border,
                            brush = Brush.linearGradient(
                                colors = listOf(Black, Black)
                            ), shape = CircleShape
                        ),
                    contentScale = ContentScale.Crop,

                    )
            } else {
                Image(
                    imageVector = viewModel.imageStateAccountAvatar.value,//viewModel.imageStateAccountAvatar.value,
                    contentDescription = "imageStore",
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = Padding.border,
                            brush = Brush.linearGradient(
                                colors = listOf(Black, Black)
                            ), shape = CircleShape
                        ),
                    contentScale = ContentScale.Crop,
                    //  colorFilter = ColorFilter.tint(colorOlivical)
                )

            }
        }
    }

    @Composable
    fun AlimentClickedHome(
        painter: Int,
        colorBottomText: Color,
        text: String,
        textColor: Color,
        bagroundAliment: Color,
        phonAliment: Color,
        onClickCall: () -> Unit,
    ) {

        Column(
            modifier = Modifier
                // .width(IntrinsicSize.Max)
                .fillMaxWidth()
                .padding(end = 30.dp)
                .background(
                    color = phonAliment,
                    shape = RoundedCornerShape(25.dp)
                ).clickable {
                    onClickCall()
                },
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Box(
                modifier = Modifier

                    .height(70.dp)
                    .width(60.dp)
                    .padding(top = 16.dp)
                    .background(
                        color = bagroundAliment,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = painter),
                    contentDescription = "Image chat",
                    modifier = Modifier
                        .size(50.dp),
                    colorFilter = ColorFilter.tint(color = BlueBlack)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .background(
                        color = colorBottomText,
                        shape = RoundedCornerShape(
                            bottomEnd = 20.dp,
                            bottomStart = 20.dp
                        )
                    ), contentAlignment = Alignment.Center
            ) {
                componetsHome.Text(
                    paddingBottom = 10.dp,
                    color = textColor,
                    text = text,
                    paddingTop = 10.dp,
                    paddingStart = 0.dp,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun IconButton(
    size: Dp,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    androidx.compose.material3.IconButton(
        modifier =
        Modifier
            .size(size),
        onClick = {
            onClick()
        },
    ) {

        Icon(
            imageVector = icon,
            contentDescription = "Add image",
            modifier = Modifier
                .size(size),
            tint = (iconColor),
        )
    }
}



