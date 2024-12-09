package com.dicoding.egglyze.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.egglyze.view.main.MainActivity
import com.dicoding.egglyze.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authHelper: FirebaseAuthHelper
    private lateinit var databaseHelper: FirebaseDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authHelper = FirebaseAuthHelper()
        databaseHelper = FirebaseDatabaseHelper()

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authHelper.loginUser(email, password) { success, message ->
                if (success) {
                    val currentUser = authHelper.getCurrentUser()
                    currentUser?.let {
                        // Ambil data pengguna dari Firebase
                        databaseHelper.getUserData(it.uid) { user ->
                            user?.let { profile ->
                                // Menyimpan data pengguna ke SharedPreferences atau UI
                                Log.d("LoginActivity", "User data: ${profile.name}, ${profile.email}")
                            } ?: run {
                                Log.d("LoginActivity", "User data not found")
                            }
                        }
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.signupText.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}
