package viewModel


import Logical.LogicalRegistrations.TextAnalyzer
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.googlecode.tesseract.android.TessBaseAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale
import java.util.concurrent.ExecutorService


class TesseractViewModel():ViewModel() {

    val bitmap: MutableState<Bitmap?> = mutableStateOf<Bitmap?>(null)
    val imageUri: MutableState<Uri?> = mutableStateOf(null)
    val textRecognized = mutableStateOf("Распознынный текст пуст ")
    val textLines = mutableStateListOf<String>()
    val text = mutableStateOf("")
    val tssStop = mutableStateOf(false)
     val tts = mutableStateOf<TextToSpeech?>(null)



    fun copyTessDataFiles(context: Context) {
        val tessDataPath = "${context.filesDir}/tessdata"
        File(tessDataPath).mkdirs()

        val assetManager = context.assets
        val files = assetManager.list("") ?: arrayOf()

        files.forEach { fileName ->
            if (fileName.endsWith(".traineddata")) {
                assetManager.open(fileName).use { inputStream ->
                    val outFile = File(tessDataPath, fileName)
                    if (!outFile.exists()) {
                        outFile.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                }
            }
        }
    }


    /*fun startRecognized(context: Context, bitmap: Bitmap, languages: String): String {
        copyTessDataFiles(context)

        val tessBaseApi = TessBaseAPI()
        tessBaseApi.init(context.filesDir.toString(), languages)
        tessBaseApi.setImage(bitmap)

        val recognizedText = tessBaseApi.utF8Text
        tessBaseApi.end()

        return recognizedText
           val textRecognized = mutableStateOf("")
    }*/


    // Метод для копирования данных Tesseract
    fun copy(context: Context) {
        val tessDataPath = File(context.filesDir, "tessdata")
        tessDataPath.mkdirs()

        val assetManager = context.assets
        val files = assetManager.list("") ?: arrayOf()

        files.forEach { fileName ->
            if (fileName.endsWith(".traineddata")) {
                val outFile = File(tessDataPath, fileName)
                if (!outFile.exists()) {
                    assetManager.open(fileName).use { inputStream ->
                        outFile.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                }
            }
        }
    }

    // Метод для распознавания текста
    fun startRecognized(context: Context, bitmap: Bitmap, languages: String): String {
        val preProcessedBitmap = preProcessBitmap(bitmap)
        copy(context)

        val tessBaseApi = TessBaseAPI()
        tessBaseApi.init(context.filesDir.toString(), languages)
        tessBaseApi.setImage( preProcessedBitmap)

        val recognizedText = tessBaseApi.utF8Text
        tessBaseApi.end()

        return recognizedText
    }
    fun preProcessBitmap(bitmap: Bitmap): Bitmap {
        // Convert to grayscale
        val grayscaleBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(grayscaleBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = filter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        // Enhance contrast
        val contrastBitmap = Bitmap.createBitmap(grayscaleBitmap.width, grayscaleBitmap.height, Bitmap.Config.ARGB_8888)
        val contrastCanvas = Canvas(contrastBitmap)
        val contrastPaint = Paint()
        val contrastMatrix = ColorMatrix()
        contrastMatrix.set(arrayOf(
            1.5f, 0f, 0f, 0f, -100f,
            0f, 1.5f, 0f, 0f, -100f,
            0f, 0f, 1.5f, 0f, -100f,
            0f, 0f, 0f, 1f, 0f
        ).toFloatArray())
        contrastPaint.colorFilter = ColorMatrixColorFilter(contrastMatrix)
        contrastCanvas.drawBitmap(grayscaleBitmap, 0f, 0f, contrastPaint)

        return contrastBitmap
    }
    fun recognizeTextAsync(context: Context, bitmap: Bitmap, languages: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val result = startRecognized(context, bitmap, languages)
                withContext(Dispatchers.Main) {
                    textRecognized.value = result
                }
            }
        }
    }

    fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    }

  fun bindCameraUseCases(
        context: Context,
        cameraProvider: ProcessCameraProvider,
        previewView: PreviewView,
        onTextDetected: (String) -> Unit,
        tesseractViewModel: TesseractViewModel,
        executor: ExecutorService,
    ) {
        try {
            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(
                        executor,
                        TextAnalyzer(context, onTextDetected, tesseractViewModel)
                    )
                }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                previewView.context as LifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalyzer
            )
        } catch (exc: Exception) {
            Log.e("CameraPreview", "Use case binding failed", exc)
            Log.e("CameraPreview", "Error details: ${exc.message}", exc)
        }
    }


    fun enhanceBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val enhancedBitmap = Bitmap.createBitmap(width, height, bitmap.config)

        val canvas = android.graphics.Canvas(enhancedBitmap)
        val paint = Paint()

        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val colorMatrixFilter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = colorMatrixFilter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        val contrast = 1.5f
        val brightness = -50f
        val contrastMatrix = ColorMatrix()
        contrastMatrix.set(
            arrayOf(
                contrast, 0f, 0f, 0f, brightness,
                0f, contrast, 0f, 0f, brightness,
                0f, 0f, contrast, 0f, brightness,
                0f, 0f, 0f, 1f, 0f
            ).toFloatArray()
        )
        paint.colorFilter = ColorMatrixColorFilter(contrastMatrix)
        canvas.drawBitmap(enhancedBitmap, 0f, 0f, paint)

        return enhancedBitmap
    }
   fun initializeTTS(context: Context) {
        tts.value = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.value?.language = Locale.getDefault()
            } else {
                Log.e("TTS", "Initialization failed")
            }
        }
    }
    fun handleLifecycle(context: Context,lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_STOP -> stopTTS()
                    Lifecycle.Event.ON_DESTROY -> tts.value?.shutdown()
                    else -> { /* Do nothing */ }
                }
            }
        })
    }
    fun stopTTS() {
        viewModelScope.launch {
            tts.value?.stop()
            tssStop.value = true
        }
    }
    fun speakText(text: String) {
        viewModelScope.launch {
            if (text.isNotEmpty()) {
                tts.value?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }
}



