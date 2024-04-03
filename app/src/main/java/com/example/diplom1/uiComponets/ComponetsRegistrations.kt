package com.example.diplom1.uiComponets

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import viewModel.RegistrationViewModel
import com.example.diplom1.ui.theme.colorOlivical
import com.example.diplom1.ui.theme.Orange
import sence.kate.practica3.padding.Padding
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.unit.TextUnit
import screen.registrationsClueText


class ComponetsRegistrations {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun OutlineTextField(
        registrationViewModel: RegistrationViewModel,
        state: MutableState<String>,
        modifier: Modifier = Modifier,
        wight: Dp,
        height: Dp,
        paddingStart: Dp,
        paddingTop: Dp,
        paddingEnd: Dp,
        paddingBottom: Dp,
        labelText: Int,


        ) {
        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            state.value,
            onValueChange = { newLogin ->
                state.value = newLogin
            },
            singleLine = true,
            textStyle = TextStyle(colorOlivical, fontSize = Padding.textSize),

            modifier = modifier
                .padding(paddingStart, paddingTop, paddingEnd, paddingBottom)
                .width(wight)
                .height(height),
            label = {
                Text(
                    text = stringResource(labelText),
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

                unfocusedBorderColor = registrationViewModel.textLabelColor.value,
                focusedBorderColor = registrationViewModel.textLabelColorClick.value,
                focusedLabelColor = registrationViewModel.textLabelColorClick.value,
                cursorColor = registrationViewModel.textLabelColorClick.value,


                ),

            shape = RoundedCornerShape(Padding.shape),
        )
    }

    //установка картинка и открытие пикера для регистрации
    @Composable
    fun PhotoPicker(
        height: Dp,
        paddingStart: Dp,
        paddingTop: Dp,
        paddingEnd: Dp,
        paddingBottom: Dp,
        wight: Dp,
        size: Dp,
        viewModel: RegistrationViewModel,
        sizeAddImage: Dp,
        colorImageTrue: Color,


        ) {

        val photoPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                viewModel.setImageUri(uri)
            }
        )
        Box(
            modifier = Modifier
                .height(height)
                .width(wight)
                .padding(
                    paddingStart,
                    paddingTop,
                    paddingEnd,
                    paddingBottom
                ),
        ) {
            Box(
                modifier = Modifier
                    .size(size)
                    .padding(
                        2.dp
                    ),
            ) {
                IconButton(
                    onClick = {
                        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        viewModel.setIconImage(viewModel.imageUriState.value);
                    },
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                ) {

                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Add image",
                        modifier = Modifier
                            .size(size)
                            .border(Padding.border, Gray, CircleShape)
                            .clip(CircleShape),


                        tint = (colorOlivical),
                    )
                    AsyncImage(
                        model = viewModel.imageUriState.value,
                        contentDescription = " Add image",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(size)
                            .fillMaxSize()
                            .border(Padding.border, colorImageTrue, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Box() {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add image",
                        modifier = Modifier
                            .size(sizeAddImage)
                            .alpha(if (viewModel.imageUriState.value == null) 1f else 0f)
                            .clip(CircleShape),
                        tint = (Orange),
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable

    fun TextField(
        registrationViewModel: RegistrationViewModel,
        state: MutableState<String>,
        wight: Dp,
        height: Dp,
        label: Int,
        paddingStart: Dp,
        paddingTop: Dp,
        paddingEnd: Dp,
        paddingBottom: Dp,
        background: Color,
        keyboardType: KeyboardType = KeyboardType.Email,
        labelColor: MutableState<Color>,
        clueText: Int,
        clueColor: Color,
        onDoneAction: () -> Unit,
        picker: () -> Unit
    ) {
        val focusManager = LocalFocusManager.current
        TextField(

            value = state.value,
            onValueChange = { newValue ->
                state.value = newValue
            },
            modifier = Modifier
                .padding(
                    paddingStart,
                    paddingTop,
                    paddingEnd,
                    paddingBottom,
                )
                .width(wight)
                .height(height)
                .background(background)
                .clickable {
                    picker()
                },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = keyboardType
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onDoneAction()//функция проверки правильного ввода
                }),
            label = {
                Text(
                    text = stringResource(label),
                    color = labelColor.value,
                    style = TextStyle(
                        fontSize = Padding.textLabelSize,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            },
            placeholder = {
                Text(
                    stringResource(id = clueText),
                    color = clueColor,
                )
            },
            textStyle = TextStyle(colorOlivical, fontSize = Padding.textSize),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = registrationViewModel.textLabelColor.value,
                focusedBorderColor = registrationViewModel.textLabelColorClick.value,
                focusedLabelColor = registrationViewModel.textLabelColorClick.value,
                cursorColor = registrationViewModel.textLabelColorClick.value,

                ),
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
        androidx.compose.material3.Button(
            modifier = Modifier
                .width(wight)
                .height(height)
                .padding(paddingStart, paddingTop, paddingEnd),
            colors = ButtonDefaults.buttonColors(buttonColor),
            onClick = {
            }
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


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DataPicker(
        registrationViewModel:RegistrationViewModel,
        size:Dp
     ){
     IconButton( modifier =
    Modifier
        .size(size)
        .clip(CircleShape),
         onClick = {
             registrationViewModel.openDateDialog.value=true
         },) {

        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "Add image",
            modifier = Modifier
                .size(size),
            tint = (colorOlivical),
        )
    }
        if(registrationViewModel.openDateDialog.value){
            val dataPickerState = rememberDatePickerState()
            val confirmEnabled = derivedStateOf{ dataPickerState.selectedDateMillis != null}
            DatePickerDialog(
                onDismissRequest = { registrationViewModel.openDateDialog.value= false },
                confirmButton = {

                    TextButton(onClick = {
                        registrationViewModel.openDateDialog.value = false
                        var date = "no Selections"
                        if(dataPickerState.selectedDateMillis != null){
                            date = registrationsClueText.convertLongToTime(dataPickerState.selectedDateMillis!!)
                        }
                        registrationViewModel.birthday.value= date
                    },
                        enabled = confirmEnabled.value
                    ) {
                        Text(text = "Okay")
                    }
                }){
                DatePicker(state = dataPickerState )

            }

        }
    }
        }





