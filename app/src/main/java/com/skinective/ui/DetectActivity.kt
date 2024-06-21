package com.skinective.ui

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skinective.databinding.ActivityDetectBinding
import com.skinective.network.api.APIService
import com.skinective.network.api.DetectResponse
import com.skinective.network.api.HistoryDetect
import com.skinective.utils.Constants
import com.skinective.utils.Constants.Companion.IS_CLICK_BUTTON_DETECT
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class DetectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetectBinding

    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val launcherIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                getResultDetect(it.data?.data)
            }
        }
    }

    private fun getResultDetect(uri: Uri?) {
        if (uri == null) return
        val file = uriToFile(uri, contentResolver)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val service = APIService.getAPIML()
        val call = service.detect(body, Constants.USER?.userId.orEmpty())
        call.enqueue(object : Callback<DetectResponse> {
            override fun onResponse(
                call: Call<DetectResponse>,
                response: Response<DetectResponse>
            ) {
                if (response.isSuccessful) {
                    IS_CLICK_BUTTON_DETECT = true
                    val result = response.body()
                    val model = HistoryDetect(
                        diseaseName = result?.data?.diseaseName.orEmpty(),
                        diseaseAction = result?.data?.diseaseAction.orEmpty(),
                        diseaseDescription = result?.data?.diseaseDescription.orEmpty(),
                        historyImgUrl = null
                    )
                    startActivity(Intent(this@DetectActivity, ResultActivity::class.java).apply {
                        putExtra("result", model)
                        putExtra("uri", uri.toString())
                    })
                } else {
                    Toast.makeText(this@DetectActivity, response.message().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DetectResponse>, t: Throwable) {
                Toast.makeText(this@DetectActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uriToFile(uri: Uri, contentResolver: ContentResolver): File {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r", null)
        val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
        val file = File.createTempFile("temp", null)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        return file
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnCapture.setOnClickListener { takePhoto() }
        binding.llUpload.setOnClickListener { openGallery() }
        binding.btnClose.setOnClickListener { finish() }
    }

    private fun openGallery() {
        val pickPhotoIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcherIntent.launch(pickPhotoIntent)
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createCustomTempFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    getResultDetect(output.savedUri)
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@DetectActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError: ${exc.message}")
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(
                    this@DetectActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    public override fun onResume() {
        super.onResume()
        startCamera()
    }

    private fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir
        return File.createTempFile(System.currentTimeMillis().toString(), ".jpg", filesDir)
    }

    companion object {
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
        private const val TAG = "DetectActivity"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val CAMERAX_RESULT = 200
    }
}