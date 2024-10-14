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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diplom1.ShedPreferences
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.ui.theme.colorOlivical
import com.googlecode.tesseract.android.TessBaseAPI
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit


class TesseractViewModel():ViewModel() {

    val bitmap: MutableState<Bitmap?> = mutableStateOf<Bitmap?>(null)
    val imageUri: MutableState<Uri?> = mutableStateOf(null)
    val textRecognized = mutableStateOf("Распознынный текст пуст ")
    val textLines = mutableStateListOf<String>()
    val text = mutableStateOf("")
    val tssStop = mutableStateOf(false)
     val tts = mutableStateOf<TextToSpeech?>(null)
    private val _isClicked = mutableStateOf(false)
    private val _isClickedBrayl = mutableStateOf(false)
    val ttsTrue =  mutableStateOf(false)
    var iconColorClick: MutableState<Color> =
        mutableStateOf(colorOlivical)
    var iconBraylColorClick: MutableState<Color> =
        mutableStateOf(colorOlivical)//устанавливаем цвет при нажатии на ввод
    val brauylTrue =  mutableStateOf(false)
  val dt = mutableStateOf(false)



    fun saveSateteButtonListen(context: Context){
        viewModelScope.launch {
            val status =  ShedPreferences.getShedPreferences(
                context = context,
                UserFileCollections = ShedPreferences.CollectionsTTs,
                keyFile = ShedPreferences.buttonState
            )
            _isClicked.value = !_isClicked.value
            if (  status== ShedPreferences.no  ){
             ShedPreferences.saveShedPreferences(
                    context = context,
                    UserFileCollections = ShedPreferences.CollectionsTTs,
                    keyFile = ShedPreferences.buttonState,
                    value = ShedPreferences.yes
                )
            //    Log.d("setTextColor", "Initial Status TTs: $ststusTTs, _isClicked: ${_isClicked.value}")
            }else{
                ShedPreferences.saveShedPreferences(
                    context = context,
                    UserFileCollections = ShedPreferences.CollectionsTTs,
                    keyFile = ShedPreferences.buttonState,
                    value = ShedPreferences.no
                )
            }
        }
    }
    fun saveSateteButtonBrayl(context: Context){
        viewModelScope.launch {
            val status =  ShedPreferences.getShedPreferences(
                context = context,
                UserFileCollections = ShedPreferences.CollectionsBrayl,
                keyFile = ShedPreferences.braylKey
            )
            Log.d("setTextColor", "Initial Status TTs: $status, _isClicked: ${_isClickedBrayl.value}")
            _isClickedBrayl.value = !_isClickedBrayl.value
            if (  status== ShedPreferences.no  ){
                ShedPreferences.saveShedPreferences(
                    context = context,
                    UserFileCollections = ShedPreferences.CollectionsBrayl,
                    keyFile = ShedPreferences.braylKey,
                    value = ShedPreferences.yes
                )

            }else{
                ShedPreferences.saveShedPreferences(
                    context = context,
                    UserFileCollections = ShedPreferences.CollectionsBrayl,
                    keyFile = ShedPreferences.braylKey,
                    value = ShedPreferences.no
                )
            }
        }
    }

    fun setTextColorListen(context: Context) {
        viewModelScope.launch {

               val ststusTTs= ShedPreferences.getShedPreferences(
                    context = context,
                    UserFileCollections = ShedPreferences.CollectionsTTs,
                    keyFile = ShedPreferences.buttonState
                )

            Log.d("setTextColor", "Initial Status TTs: $ststusTTs, _isClickedBrayl: ${_isClickedBrayl.value}")

            if (ststusTTs.toString().contains(ShedPreferences.yes)) {
                ttsTrue.value = true
                iconColorClick.value = BlueBlack
                Log.d("setTextColor", "Saving Status TTs: yes")
                ShedPreferences.saveShedPreferences(
                    context = context,
                    UserFileCollections = ShedPreferences.CollectionsTTs,
                    keyFile = ShedPreferences.keyTts,
                    value = ShedPreferences.yes
                )
              /*  Toast.makeText(context, "Голосовое соправождение активированно", Toast.LENGTH_SHORT)
                    .show()*/

            } else {
                ttsTrue.value = false
                iconColorClick.value = colorOlivical
                Log.d("setTextColor", "Saving Status TTs: no")
                ShedPreferences.saveShedPreferences(
                    context = context,
                    UserFileCollections = ShedPreferences.CollectionsTTs,
                    keyFile = ShedPreferences.keyTts,
                    value = ShedPreferences.no
                )

               /* Toast.makeText(context, "Голосовое соправождение отключено", Toast.LENGTH_SHORT)
                    .show()*/

            }

            // Проверка, что данные действительно сохранены


            //     Log.d("setTextColor", "Final Status TTs: $newStatusTTs")
        }
    }    fun setTextColorBrayl(context: Context) {
        viewModelScope.launch {

               val ststusTTs= ShedPreferences.getShedPreferences(
                    context = context,
                    UserFileCollections = ShedPreferences.CollectionsBrayl,
                    keyFile = ShedPreferences.braylKey
                )

              Log.d("setTextColor", "Initial Status TTs: $ststusTTs, _isClicked: ${_isClicked.value}")

            if (ststusTTs.toString().contains(ShedPreferences.yes)) {
                brauylTrue.value = true
                iconBraylColorClick.value = BlueBlack
                Log.d("setTextColor", "Saving Status TTs: yes")
                ShedPreferences.saveShedPreferences(
                    context = context,
                    UserFileCollections = ShedPreferences.CollectionsBrayl,
                    keyFile = ShedPreferences.braylKey,
                    value = ShedPreferences.yes
                )
               /* Toast.makeText(context, "Голосовое соправождение активированно", Toast.LENGTH_SHORT)
                    .show()*/

            } else {
                brauylTrue.value = false
                iconBraylColorClick.value = colorOlivical
                Log.d("setTextColor", "Saving Status TTs: no")
                ShedPreferences.saveShedPreferences(
                    context = context,
                    UserFileCollections = ShedPreferences.CollectionsBrayl,
                    keyFile = ShedPreferences.braylKey,
                    value = ShedPreferences.no
                )

               /* Toast.makeText(context, "Голосовое соправождение отключено", Toast.LENGTH_SHORT)
                    .show()*/

            }

            // Проверка, что данные действительно сохранены


            //     Log.d("setTextColor", "Final Status TTs: $newStatusTTs")
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
       /*tts.value = TextToSpeech(context) { status ->
           if (status == TextToSpeech.SUCCESS) {
            //   val locale = Locale("RU")
               val russianResult = tts.value?.setLanguage(Locale("ru", "RU"))
               if (russianResult == TextToSpeech.LANG_MISSING_DATA || russianResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                   Log.e("TTS", "Русский язык не поддерживается")
               } else {
                   Log.i("TTS", "Русский язык установлен")
               }

               val englishResult = tts.value?.setLanguage(Locale.ENGLISH)
               if (englishResult == TextToSpeech.LANG_MISSING_DATA || englishResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                   Log.e("TTS", "Английский язык не поддерживается")
               } else {
                   Log.i("TTS", "Английский язык установлен")
               }
           } else {
               Log.e("TTS", "Инициализация TTS не удалась")
           }*/
       }
        /*tts.value = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.value?.language = Locale.getDefault()
            } else {
                Log.e("TTS", "Initialization failed")
            }*/
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
            try {
                if (text.isNotEmpty()) {
                    tts.value?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }catch (e:Exception){
                Log.e("Kl", "ошибка при озвучивании " + e.message.toString())
            }

        }
    }

    suspend fun braylKeyboard(displayedText: MutableState<TextFieldValue>) {
        val resultDeferred = CompletableDeferred<Unit>()

        CoroutineScope(Dispatchers.Default).launch {
            try {
                startListening { letter ->
                    val currentText = displayedText.value.text
                    if (currentText != letter) {
                        displayedText.value = TextFieldValue(text = letter)
                        speakText(letter)
                    }
                }
            } catch (e: Exception) {
                resultDeferred.completeExceptionally(e)
            }
        }

        resultDeferred.await()
    }

    fun filterLetters(input: String): String {
        return input.filter { it.isLetter() }
    }

    suspend fun startListening(onLetterReceived: (String) -> Unit) {
        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("http://192.168.43.59") // Replace with your NodeMCU IP address
            .build()

        while (true) {
            try {
                withContext(Dispatchers.IO) {
                    val response: Response = client.newCall(request).execute()
                    response.use {
                        if (!it.isSuccessful) {
                            throw IOException("Unexpected response code: ${it.code}")
                        }
                        val body = it.body?.string()
                        if (body.isNullOrEmpty()) {
                            throw IOException("Empty response body")
                        }
                        val filteredBody = filterLetters(body.trim())
                        onLetterReceived(filteredBody)
                    }
                }
            } catch (e: IOException) {
                // Handle errors
            }
            delay(500) // Delay between requests
        }
    }
}



