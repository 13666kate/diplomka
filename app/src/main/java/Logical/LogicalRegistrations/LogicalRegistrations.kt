package Logical.LogicalRegistrations


import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import com.example.diplom1.ShedPreferences
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

import com.google.mlkit.vision.text.TextRecognition

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import viewModel.LoginViewModel
import viewModel.RegistrationViewModel
import viewModel.UserType


public class LogicalRegistrations() {


    @Composable
    fun imageLauncherRecognizedText(
        stateImage: MutableState<Bitmap?>,
        contextToast: Context,
        stateOrRecognized: MutableState<String>,
        regex: String,
    ): ActivityResultLauncher<Intent> {
        return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Получаем изображение из результата
                val bitmap = result.data?.extras?.get("data") as? Bitmap
                // Результат записываем во ViewModel
                stateImage.value = bitmap
                if (bitmap != null) {
                    // Вызываем переданную функцию для обработки изображения
                    LogicalRegistrations().textRecognized(
                        stateImage = bitmap,
                        sateTextOrRecognized = stateOrRecognized,
                        contextToast = contextToast,
                        regex = regex
                    )
                    Toast.makeText(contextToast, "Успешно обработано!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        contextToast,
                        "Не удалось обработать изображение",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    @Composable
    fun imageLauncherFaceDetections(
        stateImage: MutableState<Bitmap?>,
        contextToast: Context,
        stateFaceDetected: MutableState<Boolean>,
        text: MutableState<String>
    ): ActivityResultLauncher<Intent> {
        return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Получаем изображение из результата
                val bitmap = result.data?.extras?.get("data") as? Bitmap
                // Результат записываем во ViewModel
                stateImage.value = bitmap
                if (bitmap != null) {
                    // Вызываем переданную функцию для обработки изображения
                    LogicalRegistrations().faceRecognized(
                        bitmap = bitmap,
                        context = contextToast,
                        stateFaceDetected = stateFaceDetected,
                        text = text
                    )
                    Toast.makeText(contextToast, "Успешно обработано!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        contextToast,
                        "Не удалось обработать изображение",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    fun equelsImageDetectionsFace(stateBitmap: MutableState<Bitmap?>) {
        val bitmap: Bitmap = stateBitmap.value!!
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val face = FaceDetection.getClient()
        val rezult = face.process(inputImage).addOnSuccessListener { face ->
            if (face.isNotEmpty()) {
                //boundingBox получает прямоугольник охватывающий лицо
                val faceDescriptor = face[0].allLandmarks
            }

        }

    }

    fun faceRecognized(
        bitmap: Bitmap,
        context: Context,
        stateFaceDetected: MutableState<Boolean>,
        text: MutableState<String>,
    ) {


        // Настройки детектора лиц
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
            .enableTracking()
            .build()

        val faceDetector = FaceDetection.getClient(options)
        val image = InputImage.fromBitmap(bitmap, 0)
        if (image != null) {
            // Запускаем процесс обнаружения лиц
            faceDetector.process(image)
                .addOnSuccessListener { faces ->
                    if (faces.isNotEmpty()) {
                        // Лица обнаружены
                        stateFaceDetected.value = true
                        text.value = "Лица обнаружены"
                        Toast.makeText(context, "Лица обнаружены", Toast.LENGTH_SHORT).show()
                    } else {
                        // Лица не обнаружены
                        stateFaceDetected.value = false
                        text.value = "Лицо не обнаруженно"
                        Toast.makeText(context, "Лица не обнаружены", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    // Обработка ошибки обнаружения лиц
                    Log.e(TAG, "Ошибка при обнаружении лиц: ${e.message}", e)
                    stateFaceDetected.value = false
                    text.value = "Ошибка при обнаружении лиц"
                    Toast.makeText(context, "Ошибка при обнаружении лиц", Toast.LENGTH_SHORT).show()
                }
        } else {
            text.value = "Пустое изображение"
            Toast.makeText(context, "Пустое изображение", Toast.LENGTH_SHORT).show()
            stateFaceDetected.value = false
        }
    }

    @Composable
    fun cameraPermission(
        stateCameraPermissions: MutableState<Boolean>,
        contextToast: Context,
        activityResultLauncher: ActivityResultLauncher<Intent>,
    ): ActivityResultLauncher<String> {
        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted: Boolean ->
                stateCameraPermissions.value = isGranted
                if (isGranted) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 60)
                    activityResultLauncher.launch(cameraIntent)
                } else {
                    Toast.makeText(
                        contextToast,
                        "Разрешение не получено",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        )
        return cameraLauncher
    }


    fun textRecognized(
        stateImage: Bitmap,
        sateTextOrRecognized: MutableState<String>,
        contextToast: Context,
        regex: String

    ) {
        val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = stateImage.let { InputImage.fromBitmap(it, 0) }
        if (image != null) {
            // Запускаем процесс обнаружения текста
            textRecognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val textBlocks = visionText.textBlocks
                    if (textBlocks.isNotEmpty()) {
                        // Проверяем каждый текстовый блок на наличие идентификатора
                        val containsId = textBlocks.any { block ->
                            val idRegex = Regex(regex)
                            block.text.matches(regex = idRegex)
                        }
                        if (containsId) {
                            val foundText = textBlocks.joinToString(separator = "\n") { it.text }
                            val foundId = patternIdText(foundText, regex)
                            if (foundId != null) {
                                sateTextOrRecognized.value = foundId
                                Toast.makeText(
                                    contextToast,
                                    "Идентификатор найден",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } else {
                            Toast.makeText(
                                contextToast,
                                "Идентификатор не найден",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(contextToast, "Текст не обнаружен", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                .addOnFailureListener { e ->
                    // Обработка ошибки обнаружения текста
                    Log.e(TAG, "Ошибка при обнаружении текста: ${e.message}", e)
                }
        } else {
            Toast.makeText(contextToast, "Пустое изображение", Toast.LENGTH_SHORT).show()
        }
    }

    fun patternIdText(
        recognizedText: String,
        regex: String,
    ): String? {
        val idRegex = Regex(regex)
        val matchResult = idRegex.find(recognizedText)
        return matchResult?.value
    }

    fun authentifications(context: Context,
                          navHostController: NavHostController,
                          userType: UserType,
                          loginViewModel: LoginViewModel,
                          nameNavigateHome: String
    ){
        try {



           /* if (userType.userType.value == true){
              loginViewModel.saveStstusTrueUser(context)
            }else {
                loginViewModel.saveStstusFasleVolonter(context)
            }
             val userTypeVolonter = loginViewModel.getStatususerVolonter(context)
             val userTypeBlind = loginViewModel.getStatususerBlind(context)*/

            if (userType.userType.value == true) {
                loginViewModel.userAuthentication(
                    email = loginViewModel.login,
                    password = loginViewModel.Password,
                    navController = navHostController,
                    textNoRegistrations = loginViewModel.textAuthenticationsValue,
                    collectionsFireStore = "usersBlind",
                    colorOutline = loginViewModel.ColorOtline,
                    context = context,
                    nameScreenNavigations = nameNavigateHome,
                )
                val authType = userType.UserBlind.value
                ShedPreferences.saveUserType(context, authType)
            } else   {
                loginViewModel.userAuthentication(
                    email = loginViewModel.login,
                    password = loginViewModel.Password,
                    navController = navHostController,
                    textNoRegistrations = loginViewModel.textAuthenticationsValue,
                    collectionsFireStore = "usersVolonters",
                    colorOutline = loginViewModel.ColorOtline,
                    context = context,
                    nameScreenNavigations = nameNavigateHome,
                )
                val authType = userType.UserVolonters
                ShedPreferences.saveUserType(context, authType.value)
            }
        }catch (e:Exception){
            Log.e("Auth",e.message.toString())
        }
    }

}












