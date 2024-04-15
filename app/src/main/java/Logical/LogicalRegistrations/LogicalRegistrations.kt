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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

import com.google.mlkit.vision.text.TextRecognition

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import viewModel.RegistrationViewModel


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
                            //"\\bID\\d+\\b"
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

  fun createData(email:String,password:String, contextToast: Context){
      FirebaseAuth.getInstance().
      createUserWithEmailAndPassword(email,password)
          .addOnCompleteListener {
           Toast.makeText(contextToast,"Успешно ${it.isSuccessful}",Toast.LENGTH_LONG).show()
          }.addOnFailureListener{
              Toast.makeText(contextToast,"Провал ${it.message}",Toast.LENGTH_LONG).show()
              Toast.makeText(contextToast,"Провал ${it.localizedMessage}",Toast.LENGTH_LONG).show()

          }
  }
    fun registerUser(auth: FirebaseAuth, firestore: FirebaseFirestore, storage: FirebaseStorage, context: Context,
                     name: String,
                     surname: String,
                     login:String,
                     email: String,
                     password: String,
                     imageUri: Uri?,
                     documentName : String) {

      auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val userData = mapOf(
                            "name" to name,
                            "surname" to surname,
                            "email" to email,
                            "login" to login,
                            "password" to password,
                            "image" to imageUri
                            // Другие поля данных пользователя
                            // ...
                        )
                        firestore.collection(documentName).document(userId)
                            .set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(context, "вы успешно зарегистрированны", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        if (imageUri != null) {
                            val imageRef = storage.reference.child("images/${userId}/profile.jpg")
                            imageRef.putFile(imageUri)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Картинка успешео загруженна", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Ошибка : ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(context, "Error getting user ID", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Ошибка при регитсрации: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }



    }








