package com.dicoding.egglyze.view.camera

import android.Manifest
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
import com.dicoding.egglyze.R
import com.dicoding.egglyze.databinding.ActivityCameraGalleryBinding
import com.dicoding.egglyze.view.animation.LoadingSplashActivity
import com.google.api.Context

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
        ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private val PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengecek apakah izin kamera dan penyimpanan sudah diberikan
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Jika belum, minta izin
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
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
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }
    }

    // Fungsi untuk membuka galeri
    private fun startGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Untuk Android 10 dan lebih rendah, periksa izin READ_EXTERNAL_STORAGE
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                showToast("Storage permission is required to pick images")
            }
        } else {
            // Untuk Android 11 dan lebih tinggi, langsung buka picker media
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
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
            showToast("No image selected")
        }
    }


    // Fungsi untuk membuka kamera
    private fun startCamera() {
        currentImageUri = getImageUri(this)
        currentImageUri?.let { uri ->
            launcherIntentCamera.launch(uri)
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
        // Panggil LoadingSplashActivity dan teruskan URI gambar
        val intent = Intent(this, LoadingSplashActivity::class.java).apply {
            putExtra("image_uri", uri.toString())
        }
        startActivity(intent)
    }

    // Fungsi untuk mendapatkan URI gambar dari penyimpanan
    private fun getImageUri(context: Context): Uri? {
        // Logika untuk mendapatkan URI dari gambar
        return Uri.parse("content://media/external/images/media")
    }

    // Fungsi untuk menampilkan toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Izin yang diperlukan
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
