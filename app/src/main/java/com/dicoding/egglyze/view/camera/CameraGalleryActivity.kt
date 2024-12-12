package com.dicoding.egglyze.view.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.egglyze.databinding.ActivityCameraGalleryBinding
import com.dicoding.egglyze.view.animation.LoadingSplashActivity

class CameraGalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraGalleryBinding
    private var currentImageUri: Uri? = null

    // Launcher untuk meminta izin kamera
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission granted")
            } else {
                showToast("Permission denied")
            }
        }

    // Launcher untuk meminta izin penyimpanan
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.values.all { it }
            if (allGranted) {
                showToast("All permissions granted")
            } else {
                showToast("Permissions denied")
            }
        }

    // Mengecek apakah izin sudah diberikan
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private val PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengecek izin sebelum memulai
        if (!allPermissionsGranted()) {
            requestPermissionsLauncher.launch(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            )
        } else {
            // Izin sudah diberikan, lanjutkan
            startGallery()
        }

        // Listener untuk tombol galeri
        binding.galleryButton.setOnClickListener { startGallery() }

        // Listener untuk tombol kamera
        binding.cameraButton.setOnClickListener { startCamera() }

        // Listener untuk tombol analisis gambar
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let { uri ->
                analyzeImage(uri)
            }
        }
    }

    // Fungsi untuk membuka galeri
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    // Launcher untuk membuka galeri
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            Log.d("Gallery URI", "Selected URI: $uri")
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
            showToast("No image selected")
        }
    }

    // Fungsi untuk membuka kamera
    private fun startCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            currentImageUri = getImageUri(this)
            currentImageUri?.let { uri ->
                launcherIntentCamera.launch(uri)
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Launcher untuk mengambil gambar dengan kamera
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            showToast("Failed to capture image")
        }
    }

    // Fungsi untuk menampilkan gambar yang dipilih
    private fun showImage() {
        currentImageUri?.let {
            Glide.with(this)
                .load(it)
                .into(binding.previewImageView)
        }
    }

    // Fungsi untuk analisis gambar
    private fun analyzeImage(uri: Uri) {
        val intent = Intent(this, LoadingSplashActivity::class.java).apply {
            putExtra("image_uri", uri.toString())
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Izin yang diperlukan
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}

