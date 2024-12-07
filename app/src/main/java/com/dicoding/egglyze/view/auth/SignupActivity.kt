package com.dicoding.egglyze.view.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.egglyze.view.main.MainActivity
import com.dicoding.egglyze.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            // Pastikan email dan password tidak kosong
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(baseContext, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        // Logout before going to LoginActivity
                        auth.signOut()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }
        }

    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        Log.d(TAG, "Current User: ${currentUser?.email ?: "No current user"}")

        if (currentUser != null) {
            // Jika user sudah terautentikasi, arahkan ke MainActivity
            Log.d(TAG, "User already authenticated, going to MainActivity")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Menutup SignupActivity
        }
    }


}