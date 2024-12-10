package com.dicoding.egglyze.view.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.dicoding.egglyze.R
import com.dicoding.egglyze.data.remote.retrofit.ApiConfig
import com.dicoding.egglyze.databinding.ActivityCameraGalleryBinding
import com.dicoding.egglyze.view.camera.CameraActivity.Companion.CAMERAX_RESULT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class CameraGalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraGalleryBinding
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let { uri ->
                analyzeImage(uri)
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        currentImageUri?.let { uri ->
            launcherIntentCamera.launch(uri)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage(uri: Uri) {
        val filePath = uriToFile(uri) ?: run {
            showToast("Gagal memuat file gambar")
            return
        }

        val file = File(filePath)
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig.getApiService().uploadImage(body)
                withContext(Dispatchers.Main) {
                    if (response.prediction != null) {
                        showToast("Prediksi: ${response.prediction.predictedClass} dengan kepercayaan ${response.prediction.confidence}")
                        Log.d("Analyze Image", "Confidence: ${response.prediction.confidence}")
                    } else {
                        showToast("Analisis gagal: ${response.message}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Error: ${e.message}")
                    Log.e("Analyze Image", "Error: ${e.message}", e)
                }
            }
        }
    }

    private fun uriToFile(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return if (cursor != null && cursor.moveToFirst()) {
            val idx = cursor.getColumnIndex("_data")
            val path = cursor.getString(idx)
            cursor.close()
            path
        } else {
            uri.path
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
