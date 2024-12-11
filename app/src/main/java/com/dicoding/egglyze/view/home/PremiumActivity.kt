package com.dicoding.egglyze.view.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.egglyze.databinding.ActivityPremiumBinding // Import ViewBinding
import com.dicoding.egglyze.view.main.MainActivity

class PremiumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPremiumBinding // Deklarasi binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding
        binding = ActivityPremiumBinding.inflate(layoutInflater)

        // Set content view menggunakan binding.root
        setContentView(binding.root)

        // Mengakses elemen dengan binding
        binding.btnSubscribeBasic.setOnClickListener {
            // Menampilkan notifikasi untuk Basic Plan
            Toast.makeText(this, "Subscribed to Basic Plan", Toast.LENGTH_SHORT).show()

            // Kembali ke HomeActivity
            navigateToHome()
        }

        binding.btnSubscribePremium.setOnClickListener {
            // Menampilkan notifikasi untuk Premium Plan
            Toast.makeText(this, "Subscribed to Premium Plan", Toast.LENGTH_SHORT).show()

            // Kembali ke HomeActivity
            navigateToHome()
        }
    }

    // Fungsi untuk kembali ke halaman HomeActivity
    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Menutup PremiumActivity agar tidak kembali ketika tombol back ditekan
    }

}
