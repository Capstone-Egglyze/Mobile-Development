package com.dicoding.egglyze.view.animation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.egglyze.R
import com.dicoding.egglyze.data.database.DatabaseEntity
import com.dicoding.egglyze.data.database.di.Injection
import com.dicoding.egglyze.data.remote.retrofit.ApiConfig
import com.dicoding.egglyze.view.camera.ResultActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import android.animation.ObjectAnimator
import com.bumptech.glide.Glide

class LoadingSplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_splash)

        val loadingText = findViewById<TextView>(R.id.loadingText)

        val animator = ObjectAnimator.ofFloat(loadingText, "alpha", 0f, 1f).apply {
            duration = 1000 // Durasi animasi (1 detik)
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }

        val imageUri = intent.getStringExtra("image_uri")?.let { Uri.parse(it) }

        // Simulasi proses loading selama 3 detik
        Handler(Looper.getMainLooper()).postDelayed({
            imageUri?.let { uri -> analyzeImage(uri) }
        }, 3000)
    }

    private fun analyzeImage(uri: Uri) {
        try {
            val filePath = uriToFile(uri)
            if (filePath != null) {
                Log.d("AnalyzeImage", "File path: $filePath")
                uploadImage(File(filePath), uri)
            } else {
                showToast("Failed to process the image")
                Log.e("AnalyzeImage", "File path is null for URI: $uri")
            }
        } catch (e: Exception) {
            Log.e("AnalyzeImage", "Error processing image: ${e.message}", e)
            showToast("Error processing image: ${e.message}")
            finish()
        }
    }

    private fun uploadImage(file: File, uri: Uri) {
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig.getApiService().uploadImage(body)
                withContext(Dispatchers.Main) {
                    if (response.prediction != null) {
                        val confidence = response.prediction.confidence ?: "0%"
                        val predictedClass = response.prediction.predictedClass ?: "Unknown"

                        val historyEntity = DatabaseEntity(
                            image = uri.toString(),
                            predictAt = System.currentTimeMillis().toString(),
                            result = predictedClass,
                            confidence = confidence,
                            predictedClass = predictedClass
                        )

                        val databaseDao = Injection.provideDatabaseDao(this@LoadingSplashActivity)
                        Log.d("DAO", "DAO instance: $databaseDao")
                        databaseDao.insert(historyEntity)

                        val intent = Intent(this@LoadingSplashActivity, ResultActivity::class.java).apply {
                            putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
                            putExtra(ResultActivity.EXTRA_RESULT, response.prediction.predictedClass)
                            putExtra(ResultActivity.EXTRA_CONFIDENCE, response.prediction.confidence)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoadingSplashActivity,
                            "Analisis gagal: ${response.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d("LoadingSplashActivity", "Analisis gagal: ${response.message}")
                        finish()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("AnalyzeImage", "Error occurred: ${e.message}", e)
                    Toast.makeText(this@LoadingSplashActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun uriToFile(uri: Uri): String? {
        return try {
            val contentResolver = applicationContext.contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(applicationContext.filesDir, "image_${System.currentTimeMillis()}.jpg")


            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            if (file.exists()) {
                Log.d("uriToFile", "File created at: ${file.absolutePath}")
                file.absolutePath
            } else {
                Log.e("uriToFile", "File does not exist after copying")
                null
            }
        } catch (e: Exception) {
            Log.e("uriToFile", "Failed to convert URI to file: ${e.message}", e)
            null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
