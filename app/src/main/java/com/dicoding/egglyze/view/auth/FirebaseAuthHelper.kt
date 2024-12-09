package com.dicoding.egglyze.view.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class FirebaseAuthHelper {

    private val auth: FirebaseAuth = Firebase.auth

    // Fungsi untuk registrasi pengguna
    fun registerUser(
        email: String,
        password: String,
        name: String,
        onComplete: (Boolean, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Simpan data pengguna ke Realtime Database
                    val user = auth.currentUser
                    user?.let {
                        saveUserData(user.uid, name, email, onComplete)
                    }
                } else {
                    onComplete(false, "Registration failed: ${task.exception?.message}")
                }
            }
    }

    // Fungsi untuk login
    fun loginUser(email: String, password: String, onComplete: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, "Login successful")
                } else {
                    onComplete(false, "Login failed: ${task.exception?.message}")
                }
            }
    }

    // Fungsi untuk logout
    fun logoutUser() {
        auth.signOut()
    }

    // Fungsi untuk menyimpan data pengguna ke Firebase Realtime Database
    private fun saveUserData(
        userId: String,
        name: String,
        email: String,
        onComplete: (Boolean, String) -> Unit
    ) {
        val database = FirebaseDatabase.getInstance().reference

        // Gunakan model UserProfile yang Anda buat
        val userProfile = UserProfile(name = name, email = email)

        // Simpan ke dalam node "users" di Firebase Realtime Database
        database.child("users").child(userId).setValue(userProfile)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, "User data saved successfully")
                } else {
                    onComplete(false, "Failed to save user data: ${task.exception?.message}")
                }
            }
    }

    // Mendapatkan pengguna saat ini
    fun getCurrentUser() = auth.currentUser
}
