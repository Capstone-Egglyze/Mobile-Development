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
            permissions.entries.forEach { entry ->
                Log.d("Permission Check", "${entry.key} granted: ${entry.value}")
            }
            val allGranted = permissions.values.all { it }
            if (allGranted) {
                Log.d("Permission", "Semua izin diberikan")
                showToast("All permissions granted")
            } else {
                Log.e("Permission", "Beberapa izin ditolak")
                showToast("Permissions denied")
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("Lifecycle", "Activity Created")

        // Mengecek izin sebelum memulai
        if (!allPermissionsGranted()) {
            Log.d("Permission", "Meminta izin kamera dan penyimpanan")
            requestPermissionsLauncher.launch(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            )
        } else {
            // Izin sudah diberikan, lanjutkan
            Log.d("Permission", "Semua izin sudah diberikan")
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
            } ?: Log.w("Image Analysis", "Gambar belum dipilih")
        }
    }

    // Mengecek apakah izin sudah diberikan
    private fun allPermissionsGranted(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        Log.d("Permission Status", "Kamera: ${cameraPermission == PackageManager.PERMISSION_GRANTED}")
        Log.d("Permission Status", "Penyimpanan: ${storagePermission == PackageManager.PERMISSION_GRANTED}")

        return cameraPermission == PackageManager.PERMISSION_GRANTED &&
                storagePermission == PackageManager.PERMISSION_GRANTED
    }

    // Fungsi untuk membuka galeri
    private fun startGallery() {
        Log.d("Action", "Memulai galeri")
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    // Launcher untuk membuka galeri
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            Log.d("Gallery URI", "URI yang dipilih: $uri")
            currentImageUri = uri
            showImage()
        } else {
            Log.w("Gallery URI", "Tidak ada media yang dipilih")
            showToast("No image selected")
        }
    }

    // Fungsi untuk membuka kamera
    private fun startCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Action", "Memulai kamera")
            currentImageUri = getImageUri(this)
            currentImageUri?.let { uri ->
                launcherIntentCamera.launch(uri)
            }
        } else {
            Log.w("Permission", "Meminta izin kamera")
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
            Log.d("Image Preview", "Menampilkan gambar dengan URI: $it")
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

