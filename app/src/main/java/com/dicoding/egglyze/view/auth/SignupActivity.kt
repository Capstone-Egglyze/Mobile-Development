package com.dicoding.egglyze.view.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.egglyze.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var authHelper: FirebaseAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authHelper = FirebaseAuthHelper()

        // Listener untuk tombol signup
        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
            val name = binding.nameEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Email, Password dan Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Password dan Konfirmasi Password tidak sama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Registrasi pengguna menggunakan Firebase
            authHelper.registerUser(email, password, name) { success, message ->
                if (success) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Menambahkan listener untuk tombol login
        binding.loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
