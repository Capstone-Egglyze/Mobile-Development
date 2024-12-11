package com.dicoding.egglyze.view.camera

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.egglyze.R
import com.dicoding.egglyze.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val result = intent.getStringExtra(EXTRA_RESULT)
        val confidence = intent.getStringExtra(EXTRA_CONFIDENCE) ?: "N/A"

        imageUri?.let {
            binding.resultImage.setImageURI(it)
        }
        val confidenceText = if (confidence != "N/A") {
            "Confidence: $confidence"
        } else {
            getString(R.string.analysis_failed)
        }
        binding.resultTextView.text = "$result\n$confidenceText"
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_CONFIDENCE = "extra_confidence"
    }
}
