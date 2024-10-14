package screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.diplom1.R
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.Orange
import com.example.diplom1.ui.theme.colorOlivical
import sence.kate.practica3.padding.Padding
import viewModel.RegistrationViewModel
import viewModel.TesseractViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@Composable
fun CameraPreview(
    context: Context,
    onTextDetected: (String) -> Unit,
    onClick: () -> Unit,
    tesseractViewModel: TesseractViewModel
) {
    val iconSize = 60.dp
    val iconColor = colorOlivical
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BlueBlack)
    ) {
        IconButton(
            modifier =
            Modifier
                .size(iconSize),
            onClick = {
                tesseractViewModel.tts.value?.stop()
                tesseractViewModel.tssStop.value = true
                onClick()
            },
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Add image",
                modifier = Modifier
                    .size(iconSize),
                tint = (iconColor),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(top = 20.dp)
                .padding(30.dp)
        ) {

            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        tesseractViewModel.bindCameraUseCases(
                            context = ctx,
                            cameraProvider = cameraProvider,
                            previewView = previewView,
                            onTextDetected = onTextDetected,
                            tesseractViewModel = tesseractViewModel,
                            executor = cameraExecutor,
                        )
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .clip(RoundedCornerShape(16.dp))

            )
            androidx.compose.material3.Text(
                text = tesseractViewModel.text.value,
                modifier = Modifier
                    .padding(16.dp),
                style = TextStyle(
                    color = colorOlivical,
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.registration, FontWeight.Bold)),
                    textAlign = TextAlign.Center
                )
            )
        }
    }

    SpeakText(
        tesseractViewModel.text.value,
        context = context,
        tesseractViewModel = tesseractViewModel
    )
}

@Composable
fun SpeakText(
    text: String,
    context: Context,
    tesseractViewModel: TesseractViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        tesseractViewModel.initializeTTS(context)
    }
    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = androidx.lifecycle.LifecycleEventObserver { _, event ->
            when (event) {
                androidx.lifecycle.Lifecycle.Event.ON_STOP -> {
                    tesseractViewModel.stopTTS()
                }

                androidx.lifecycle.Lifecycle.Event.ON_DESTROY -> {
                    tesseractViewModel.tts.value?.shutdown()
                }

                else -> {

                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            tesseractViewModel.tts.value?.shutdown()
        }
    }

    if (tesseractViewModel.tssStop.value) {
        tesseractViewModel.stopTTS()
        tesseractViewModel.tssStop.value = false
    }

    if (text.isNotEmpty()) {
        tesseractViewModel.speakText(text)
    }
    BackHandler {
        tesseractViewModel.stopTTS()
    }
}

@Composable
fun CameraScreen(
    context: Context,
    onClickEyes: () -> Unit,
    onClickCameraAndGallery: () -> Unit,
    onBack: () -> Unit,
    tesseractViewModel: TesseractViewModel,
) {

    val iconSize = 130.dp
    val iconColor = colorOlivical

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBlack),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), horizontalAlignment = Alignment.Start
        ) {
            componetsRegistrations.IconButton(
                size = 60.dp,
                icon = Icons.Default.ArrowBack,
                iconColor = colorOlivical,
                onClick = onBack
            )
        }
        Column(
            modifier = Modifier
                .padding(bottom = 80.dp, top = 60.dp),
            verticalArrangement = Arrangement.Center
        ) {

            componetsRegistrations.IconButtonImage(
                size = 170.dp,
                icon = painterResource(id = R.drawable.cameraandgallery),
                iconColor = iconColor,
                onClick = onClickCameraAndGallery
            )
        }
        componetsRegistrations.IconButtonImage(
            size = iconSize,
            icon = painterResource(id = R.drawable.textanaliz),
            iconColor = iconColor,
            onClick = onClickEyes
        )
    }
}

@Composable
fun GalleryAndCameraScreen(
    context: Context,
    tesseractViewModel: TesseractViewModel,
    registrationViewModel: RegistrationViewModel,
    onBack: () -> Unit
) {
    val iconSize = 100.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBlack)
            .verticalScroll(rememberScrollState()),
    ) {
        val pickImageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            tesseractViewModel.imageUri.value = uri
            tesseractViewModel.bitmap.value =
                uri?.let { tesseractViewModel.loadBitmapFromUri(context, it) }
            tesseractViewModel.bitmap.value?.let { bitmap ->
                tesseractViewModel.recognizeTextAsync(context, bitmap, "rus+eng+kir")
            }
        }
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(10.dp)
        ) {
            componetsRegistrations.IconButton(
                size = 60.dp,
                icon = Icons.Default.ArrowBack,
                iconColor = colorOlivical,
                onClick = onBack
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 50.dp,
                        end = 50.dp,
                        top = 30.dp,
                        bottom = 30.dp,
                    ),
                horizontalArrangement = Arrangement.Center
            ) {
                val takePictureLauncher =
                    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            val bitmap = result.data?.extras?.get("data") as? Bitmap
                            tesseractViewModel.bitmap.value = bitmap
                            bitmap?.let {
                                tesseractViewModel.recognizeTextAsync(context, it, "rus+eng+kir")
                            }
                        }
                    }

                val cameraPermissions = cameraPermission(
                    stateCameraPermissions = registrationViewModel.isCameraPermission,
                    contextToast = context,
                    activityResultLauncher = takePictureLauncher
                )

                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    componetsRegistrations.IconButtonImage(
                        size = iconSize,
                        icon = painterResource(id = R.drawable.baseline_add_photo_alternate_24),
                        iconColor = colorOlivical
                    ) {
                        pickImageLauncher.launch("image/*")
                    }
                }

                componetsRegistrations.IconButtonImage(
                    size = iconSize,
                    icon = painterResource(id = R.drawable.camera_alt_24),
                    iconColor = colorOlivical
                ) {
                    cameraPermissions.launch(android.Manifest.permission.CAMERA)
                }
            }

            tesseractViewModel.bitmap.value?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .border(
                            width = Padding.border,
                            brush = Brush.linearGradient(
                                colors = listOf(colorOlivical, Orange)
                            ), shape = RectangleShape
                        ),
                )
            }

            if (tesseractViewModel.bitmap.value == null) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_crop_original_24),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colorOlivical),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, end = 40.dp)
                        .size(300.dp)
                )
            }
        }
        val lifecycleOwner = LocalLifecycleOwner.current

        // Инициализация TTS и привязка к жизненному циклу
        LaunchedEffect(lifecycleOwner) {
            tesseractViewModel.initializeTTS(context)
            tesseractViewModel.handleLifecycle(context, lifecycleOwner)
        }

        androidx.compose.material3.Text(
            tesseractViewModel.textRecognized.value,

            modifier = Modifier
                .padding(top = 20.dp, start = 0.dp)
                .fillMaxWidth()
                .clickable {
                    if (tesseractViewModel.textRecognized.value.isNotEmpty()) {
                        tesseractViewModel.speakText(tesseractViewModel.textRecognized.value)
                    }
                },
            style = TextStyle(
                color = colorOlivical,
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.osnova, FontWeight.Bold)),
                textAlign = TextAlign.Center
            )

        )
    }
}

// Composable function for camera permission
@Composable
fun cameraPermission(
    stateCameraPermissions: MutableState<Boolean>,
    contextToast: Context,
    activityResultLauncher: ActivityResultLauncher<Intent>
): ActivityResultLauncher<String> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            stateCameraPermissions.value = isGranted
            if (isGranted) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                activityResultLauncher.launch(cameraIntent)
            } else {
                Toast.makeText(
                    contextToast,
                    "Разрешение не получено",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )
}


/*
@Preview
@Composable
 fun MyScreen(){
     val context = LocalContext.current
    val  tess = TesseractViewModel()
    GalleryAndCameraScreen(context=context, tesseractViewModel = tess)
 }
*/


// Extension function to convert ImageProxy to Bitmap


// Extension function to convert ImageProxy to Bitmap


/*@Composable
fun TextRecognized(
    context: Context,
    tesseractViewModel: TesseractViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBlack)
    ) {
       val  tess = Tesseract(context)
        val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
        // Инициализируем CameraProvider
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        val pickImageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            tesseractViewModel.imageUri.value = uri
            tesseractViewModel.bitmap.value = uri?.let { tesseractViewModel.loadBitmapFromUri(context, it) }
        }


        Button(onClick = {
            pickImageLauncher.launch("image/*")
        }) {
            androidx.compose.material.Text(text = "Выбрать фото")
        }

        tesseractViewModel.bitmap.value?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )


            Button(onClick = {
                try {
                    tesseractViewModel.textRecognized.value = tesseractViewModel.startRecognized(context = context, bitmap = tesseractViewModel.bitmap.value!!, languages ="rus+eng+kir" )

                }catch (e:Exception){
                    Log.e("Call","ошибка распознавания"+ e.message.toString())
                }
            }) {
                Text("Распознать текст")
            }

           // Spacer(modifier = Modifier.height(16.dp))

            Text("Распознанный текст:")
            Text(tesseractViewModel.textRecognized.value, modifier = Modifier.padding(top = 8.dp),
                color = Color.White)
        }
    }
}*/

 */
//val tesseractViewModel: TesseractViewModel = TesseractViewModel()
/*@Composable
fun CameraScreen( context: Context, tesseractViewModel: TesseractViewModel = viewModel()) {
    // Объявляем состояние для хранения обнаруженного текста
    val detectedText = remember { mutableStateOf("") }

    Column {

        // Вывод предварительного просмотра камеры
        CameraPreview(context = context, tesseractViewModel=tesseractViewModel, onTextDetected = {text ->
           tesseractViewModel.textRecognized.value= text
        })

        // Отображение распознанного текста
        Text(
            text = tesseractViewModel.textRecognized.value,
            modifier = Modifier.padding(16.dp)
        )
    }
}*/


/*@Composable
fun CameraPreview(
context: Context,
onBitmapCaptured: (Bitmap) -> Unit
) {
AndroidView(factory = { ctx ->
    SurfaceView(ctx).apply {
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                // Открываем камеру и устанавливаем предварительный просмотр
                val camera = Camera.open()
                try {
                    camera.parameters.setRotation(90)
                    camera.parameters.setPreviewSize(500, 500)
                    camera.setPreviewDisplay(holder)
                    camera.startPreview()
                    camera.setDisplayOrientation(90)
                    // Обработка захвата изображения
                    camera.takePicture(null, null, object : Camera.PictureCallback {
                        override fun onPictureTaken(data: ByteArray?, camera: Camera?) {
                            data?.let {
                                val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                                onBitmapCaptured(bitmap)
                            }
                            // Закрываем камеру после захвата изображения
                            camera?.stopPreview()
                            camera?.release()
                        }
                    })
                } catch (e: IOException) {
                    Log.e("CameraPreview", "Error setting camera preview: ${e.message}")
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
            override fun surfaceDestroyed(holder: SurfaceHolder) {}
        })
    }
})
}*/
/*

@Composable
fun CameraPreview(
    context: Context,
    onTextDetected: (String) -> Unit,
    tesseractViewModel: TesseractViewModel
) {
    AndroidView(factory = { ctx ->
        SurfaceView(ctx).apply {
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
            holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    // Открываем камеру и устанавливаем предварительный просмотр
                    val camera = Camera.open()
                    try {
                        camera.parameters.setRotation(90)
                        camera.parameters.setPreviewSize(500, 500)
                        camera.setPreviewDisplay(holder)
                        camera.startPreview()
                        camera.setDisplayOrientation(90)

                        // Обработка предварительного просмотра для обнаружения текста
                        camera.setOneShotPreviewCallback { data, _ ->
                            data?.let { byteArray ->
                                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                                if (bitmap != null) {
                                    val recognizedText = bitmap?.let {
                                        tesseractViewModel.startRecognized(
                                            context = context,
                                            bitmap = it,
                                            "eng"
                                        )
                                    }
                                    onTextDetected(recognizedText.toString())
                                } else {
                                    Log.e("CameraPreview", "Bitmap is null")
                                }
                            } ?: run {
                                Log.e("CameraPreview", "Data is null")
                                // Например, вывод сообщения об ошибке или выполнение другой логики
                            }
                        }
                    } catch (e: IOException) {
                        Log.e("CameraPreview", "Error setting camera preview: ${e.message}")
                    }
                }

                override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
                override fun surfaceDestroyed(holder: SurfaceHolder) {}
            })
        }
    })
}
*/


/*
@Preview
@Composable
fun MyScreen(){
    val context = LocalContext.current
    val tess = TesseractViewModel()
    //TextRecognized(context = context, tesseractViewModel = tess)
}*/
