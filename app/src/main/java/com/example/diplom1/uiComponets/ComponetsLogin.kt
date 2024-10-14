package com.example.diplom1.uiComponets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.example.diplom1.R
import com.example.diplom1.ui.theme.colorOlivical
import sence.kate.practica3.padding.Padding
import viewModel.LoginViewModel
import viewModel.TesseractViewModel

class ComponetsLogin {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun OutlineTextField(
        viewModel: LoginViewModel,
        state: MutableState<String>,
        modifier: Modifier = Modifier,
        colorOutline:MutableState<Color>,
        wight: Dp,
        height: Dp,
        padding: Dp,
        labelText: Int,
        text:String,
        tesseractViewModel: TesseractViewModel,


        ) {
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            state.value,
            onValueChange = { newLogin ->
                state.value = newLogin
                tesseractViewModel.speakText( state.value)
            },
            singleLine = true,
            textStyle = TextStyle(colorOlivical, fontSize = Padding.textSize),
            modifier = modifier
                .padding(padding)
                .width(wight)
                .height(height)
                ,
            label = {
                Text(
                    text = stringResource(labelText),
                    modifier = Modifier.
                        clickable {
                            tesseractViewModel.speakText(text = text)
                        },
                    color = colorOlivical,
                    style = TextStyle(
                        fontSize = Padding.textLabelSize,
                        fontWeight = FontWeight.Bold
                    ),
                )
            },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done, keyboardType = KeyboardType.Email
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = colorOutline.value,
                focusedBorderColor = viewModel.textLabelColorClick.value,
                focusedLabelColor = viewModel.textLabelColorClick.value,
                cursorColor = viewModel.textLabelColorClick.value
            ),
            shape = RoundedCornerShape(Padding.shape),
        )
    }

    @Composable
    fun Button(
        onClick: () -> Unit,
        wight: Dp,
        height: Dp,
        buttonColor: Color,
        textSize: TextUnit,
        textColor: Color,
        labelText: Int,
        paddingTop: Dp,
        paddingEnd: Dp,
        paddingStart: Dp
    ) {
        Button(
            modifier = Modifier
                .width(wight)
                .height(height)
                .padding(paddingStart, paddingTop, paddingEnd),
            colors = ButtonDefaults.buttonColors(buttonColor),
            onClick = { onClick() }
        ) {
            Text(
                text = stringResource(labelText),
                style = TextStyle(
                    color = textColor,
                    fontSize = textSize,
                    fontWeight = FontWeight.Bold,
                )
            )
        }
    }

    @Composable
    fun TextBold(
        viewModel: LoginViewModel,
        textSize: TextUnit,
        labelText: Int,
        paddingStart: Dp,
        paddingTop: Dp,
        paddingEnd: Dp,
        text: String,
        tesseractViewModel: TesseractViewModel,
        onClick: () -> Unit
    ) {
        Text(
            text = stringResource(labelText),
            modifier = Modifier
                .padding(paddingStart, paddingTop, paddingEnd)
                .clickable {
                    viewModel.setTextColor()
                    tesseractViewModel.speakText(text = text)

                    onClick()},
            style = TextStyle(
                color = viewModel.textColor.value,
                fontSize = textSize,
                fontFamily = FontFamily(Font(R.font.registration, FontWeight.Bold)),
                textAlign = TextAlign.Center
            )

        )

    }

    @Composable
    fun Image(
        image: Int,
        wight: Dp,
        height: Dp,
        paddingTop: Dp,
        paddingStart: Dp,
        paddingEnd: Dp,
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Login",
            modifier = Modifier
                .size(wight, height)
                .padding(paddingStart, paddingTop, paddingEnd)
        )

    }

  }











