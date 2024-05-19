package com.example.diplom1.uiComponets

import Logical.LogicalRegistrations.LogicalRegistrations
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.diplom1.ui.theme.BlueBlack


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
        setImageUri: (Uri?) -> Unit,
        imageUriState: State<Uri?>

    ) {

        val photoPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                setImageUri(uri)
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
                        viewModel.setIconImage(imageUriState.value);

                        //   setIconImage(uriImage.value)

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
                        model = imageUriState.value,
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
        picker: () -> Unit,
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
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    registrationViewModel.textSize.value = coordinates.size.toSize()
                }
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
                    text = stringResource(id = clueText),
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
        paddingStart: Dp,
        //  enabled: Boolean
    ) {
        androidx.compose.material3.Button(
            modifier = Modifier
                .width(wight)
                .height(height)
                .padding(paddingStart, paddingTop, paddingEnd),
            colors = ButtonDefaults.buttonColors(buttonColor),
            onClick = {

            },
            //  enabled = enabled,

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


    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DataPicker(
        registrationViewModel: RegistrationViewModel,
        size: Dp
    ) {
        IconButton(
            modifier =
            Modifier
                .size(size)
                .clip(CircleShape),
            onClick = {
                registrationViewModel.openDateDialog.value = true
            },
        ) {

            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Add image",
                modifier = Modifier
                    .size(size),
                tint = (colorOlivical),
            )
        }
        if (registrationViewModel.openDateDialog.value) {
            val dataPickerState = rememberDatePickerState()
            val confirmEnabled = derivedStateOf { dataPickerState.selectedDateMillis != null }
            DatePickerDialog(
                onDismissRequest = { registrationViewModel.openDateDialog.value = false },
                confirmButton = {

                    TextButton(
                        onClick = {
                            registrationViewModel.openDateDialog.value = false
                            var date = "no Selections"
                            if (dataPickerState.selectedDateMillis != null) {
                                date =
                                    registrationViewModel.convertLongToTime(dataPickerState.selectedDateMillis!!)
                            }
                            registrationViewModel.birthday.value = date
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text(text = "Okay")
                    }
                }) {
                DatePicker(state = dataPickerState)

            }

        }
    }

    @Composable
    fun TextRecognizedOrCamera(
        registrationViewModel: RegistrationViewModel,
        state: MutableState<String>,
        wight: Dp,
        height: Dp,
        label: Int,
        paddingStart: Dp,
        paddingTop: Dp,
        paddingEnd: Dp,
        paddingBottom: Dp,
        contextToast: Context,
        background: Color,
        labelColor: MutableState<Color>,
        clueText: Int,
        clueColor: Color,
        iconBittonSize: Dp,
        icon: ImageVector,
        iconColor: Color,
        staTextOrReognezed: MutableState<String>,
        stateImage: MutableState<Bitmap?>,
        stateOrPermissions: MutableState<Boolean>,
        regex: String,
        actions: () -> Unit,

        ) {

        TextField(
            registrationViewModel = registrationViewModel,
            state = state,
            wight = wight,
            height = height,
            label = label,
            paddingStart = paddingStart,
            paddingTop = paddingTop,
            paddingEnd = paddingEnd,
            paddingBottom = paddingBottom,
            background = background,
            labelColor = labelColor,
            clueText = clueText,
            clueColor = clueColor,
            onDoneAction = { actions() }) {

        }
        val takePicture = LogicalRegistrations().imageLauncherRecognizedText(
            stateImage = stateImage,
            contextToast = contextToast,
            stateOrRecognized = staTextOrReognezed,
            regex = regex,
        )
        val cameraPermissions = LogicalRegistrations().cameraPermission(
            stateCameraPermissions = stateOrPermissions,
            contextToast = contextToast,
            activityResultLauncher = takePicture,

            )
        IconButton(
            size = iconBittonSize,
            icon = icon,
            iconColor = iconColor,
        ) {
            cameraPermissions.launch(Manifest.permission.CAMERA)
        }
    }

    @Composable
    fun FaceDetectionsID(
        iconBittonSize: Dp,
        icon: ImageVector,
        iconColor: Color,
        stateOrPermissions: MutableState<Boolean>,
        contextToast: Context,
        stateImage: MutableState<Bitmap?>,
        stateFaceDetection: MutableState<Boolean>,
        text: MutableState<String>,
        colorText: MutableState<Color>,

        ) {
        val takePicture = LogicalRegistrations().imageLauncherFaceDetections(
            stateImage = stateImage,
            contextToast = contextToast,
            stateFaceDetected = stateFaceDetection,
            text = text
        )
        val cameraPermissions = LogicalRegistrations().cameraPermission(
            stateCameraPermissions = stateOrPermissions,
            contextToast = contextToast,
            activityResultLauncher = takePicture,


            )
        Text(
            text = text.value,
            color = colorText.value,
            style = TextStyle(
                fontSize = Padding.textLabelSize,
                fontWeight = FontWeight.Bold,
            )
        )
        IconButton(
            size = iconBittonSize,
            icon = icon,
            iconColor = iconColor,
        ) {
            cameraPermissions.launch(Manifest.permission.CAMERA)
        }
    }

    @Composable
    fun FaceDetectionsPhoto(
        iconBittonSize: Dp,
        icon: ImageVector,
        iconColor: Color,
        stateOrPermissions: MutableState<Boolean>,
        contextToast: Context,
        stateImage: MutableState<Bitmap?>,
        stateFaceDetection: MutableState<Boolean>,
        text: MutableState<String>,
        colorText: MutableState<Color>,
    ) {
        val takePicture = LogicalRegistrations().imageLauncherFaceDetections(
            stateImage = stateImage,
            contextToast = contextToast,
            stateFaceDetected = stateFaceDetection,
            text = text
        )
        val cameraPermissions = LogicalRegistrations().cameraPermission(
            stateCameraPermissions = stateOrPermissions,
            contextToast = contextToast,
            activityResultLauncher = takePicture,

            )
        Text(
            text = text.value,
            color = colorText.value,
            style = TextStyle(
                fontSize = Padding.textLabelSize,
                fontWeight = FontWeight.Bold,
            )
        )
        IconButton(
            size = iconBittonSize,
            icon = icon,
            iconColor = iconColor,
        ) {
            cameraPermissions.launch(Manifest.permission.CAMERA)
        }
    }

    @Composable
    fun IconButton(
        size: Dp,
        icon: ImageVector,
        iconColor: Color,
        onClick: () -> Unit
    ) {
        IconButton(
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

    @Composable
    fun ListOrganizations(
        registrationViewModel: RegistrationViewModel,
        paddingStart: Dp,
        paddingTop: Dp,
        paddingEnd: Dp,
        paddingBottom: Dp,
        wight: Dp,
        height: Dp,
        label: Int,
        labelColor: MutableState<Color>,
        clueColor: Color,
        clueText: Int,
        background: Color,
        onDoneAction: () -> Unit,
        size: Dp,
        textList:MutableState<String>,
        dropDownList:List<String>,

    ) {

        var expanded = remember { mutableStateOf(false) }

        val icon = if (expanded.value)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown


        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {

            TextField(
                registrationViewModel = registrationViewModel,
                state = textList,
                wight = wight,
                height = height,
                label = label,
                paddingStart = paddingStart,
                paddingTop = paddingTop,
                paddingEnd = paddingEnd,
                paddingBottom = paddingBottom,
                background = background,
                labelColor = labelColor,
                clueText = clueText,
                clueColor = clueColor,
                onDoneAction = { onDoneAction() }) {

            }

            Icon(
                icon, "contentDescription",
                Modifier
                    .clickable
                    {
                        try{
                            expanded.value = !
                            expanded.value
                        }catch (e:Exception){
                            Log.e("icon", e.message.toString())
                        }

                    }
                    .size(size),
                tint = (colorOlivical),
            )
        }
        Column( modifier = Modifier
            .background(color = colorOlivical),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {


            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = {expanded.value = false },
                modifier = Modifier
                    .background(
                        color = colorOlivical,
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    /*.width(
                        with(LocalDensity.current) {
                            registrationViewModel.textSize.value.width.toDp()
                        },
                    )*/
                    .width(200.dp)
                    .height(300.dp)
                    //.width(50.dp).height(100.dp)
            ) {
                dropDownList.forEach { label ->
                    DropdownMenuItem(onClick = {
                        try {
                            textList.value = label
                            expanded.value = false
                        }catch (e:Exception){
                        Log.e("icon", e.message.toString())
                    }
                    }, modifier = Modifier
                        .background(colorOlivical,)) {
                        Text(text = label,
                            color = BlueBlack,
                            style = TextStyle(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            ),)
                    }
                }
            }
        }
    }
    }








