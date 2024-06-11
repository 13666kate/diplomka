package Logical.LogicalRegistrations

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import viewModel.TesseractViewModel

class TextAnalyzer(
    private val context: Context,
    private val onTextDetected: (String) -> Unit,
    private val tesseractViewModel: TesseractViewModel
) : ImageAnalysis.Analyzer {

    private var isProcessing = false

    override fun analyze(imageProxy: ImageProxy) {
        if (isProcessing) {
            imageProxy.close()
            return
        }

        val bitmap = imageProxy.toBitmap()
        if (bitmap != null) {
            isProcessing = true
            try {
                val enhancedBitmap = TesseractViewModel().enhanceBitmap(bitmap)
                val recognizedText =
                    tesseractViewModel.startRecognized(context, enhancedBitmap, "rus+eng+kir")

                if (recognizedText.isNotBlank()) {
                    onTextDetected(recognizedText)
                }
            } catch (e: Exception) {
                Log.e("CameraPreview", "Error during text recognition", e)
            } finally {
                imageProxy.close()
                isProcessing = false
            }
        } else {
            Log.e("CameraPreview", "Bitmap is null")
            imageProxy.close()
        }
    }
}

/*
fun ImageProxy.toBitmap(): Bitmap? {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    val image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

    val rotationDegrees = imageInfo.rotationDegrees

    val matrix = Matrix().apply {
        postRotate(rotationDegrees.toFloat())
    }
    val rotatedImage = Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)

    close()
    return rotatedImage
}
*/
