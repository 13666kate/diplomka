package Logical.LogicalRegistrations

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.SurfaceHolder
import androidx.annotation.RequiresPermission
import java.io.IOException


class Tesseract(private val context: Context) {


    // Получаем экземпляр менеджера камер
    private val cameraManager: CameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var cameraDevice: CameraDevice? = null

    // Функция для открытия камеры
    @RequiresPermission(android.Manifest.permission.CAMERA)
    fun openCamera(cameraId: String, previewSurface: Surface, handler: Handler, listener: CameraDevice.StateCallback) {
        try {
            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                // Камера успешно открыта
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    createCameraPreviewSession(previewSurface, handler)
                }

                // Камера отключена
                override fun onDisconnected(camera: CameraDevice) {
                    cameraDevice?.close()
                    cameraDevice = null
                }

                // Возникла ошибка при открытии камеры
                override fun onError(camera: CameraDevice, error: Int) {
                    cameraDevice?.close()
                    cameraDevice = null
                }
            }, handler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }
    private fun createCameraPreviewSession(previewSurface: Surface, handler: Handler) {
        try {
            cameraDevice?.createCaptureSession(listOf(previewSurface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    try {
                        val captureRequestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                        captureRequestBuilder?.addTarget(previewSurface)
                        session.setRepeatingRequest(captureRequestBuilder!!.build(), null, handler)
                    } catch (e: CameraAccessException) {
                        e.printStackTrace()
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {}
            }, handler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    // Функция для закрытия камеры
    fun closeCamera() {
        cameraDevice?.close()
        cameraDevice = null
    }

    // Функция для получения оптимального размера предварительного просмотра
    fun getOptimalPreviewSize(cameraId: String, maxWidth: Int, maxHeight: Int): Size? {
        return try {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val sizes = map?.getOutputSizes(SurfaceTexture::class.java) ?: emptyArray()
            sizes.firstOrNull { it.width <= maxWidth && it.height <= maxHeight }
        } catch (e: CameraAccessException) {
            null
        }
    }

    private val TAG = "CameraPreview"
    private var mCamera: Camera? = null

    /*init {
        holder.addCallback(this)
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }*/

     fun surfaceCreated(holder: SurfaceHolder) {
        mCamera = Camera.open()
        try {
            mCamera?.setPreviewDisplay(holder)
            mCamera?.startPreview()
        } catch (e: IOException) {
            Log.d(TAG, "Error setting camera preview: ${e.message}")
        }
    }

    fun surfaceDestroyed(holder: SurfaceHolder) {
        mCamera?.stopPreview()
        mCamera?.release()
        mCamera = null
    }

     fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
        if (holder.surface == null) {
            return
        }
        try {
            mCamera?.stopPreview()
            mCamera?.setPreviewDisplay(holder)
            mCamera?.startPreview()
        } catch (e: Exception) {
            Log.d(TAG, "Error starting camera preview: ${e.message}")
        }
    }
}

