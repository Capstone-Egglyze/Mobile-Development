package com.dicoding.egglyze.view.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.egglyze.MainActivity
import com.dicoding.egglyze.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth

        Handler(Looper.getMainLooper()).postDelayed({
            checkUserLoginStatus()
        }, 3000L)
    }

    private fun checkUserLoginStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("SplashActivity", "User logged in: ${currentUser.email}")
            // User logged in, navigate to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Log.d("SplashActivity", "No user logged in")
            // No user logged in, navigate to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}