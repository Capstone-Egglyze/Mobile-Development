package com.dicoding.egglyze.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.egglyze.view.profile.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun loadUserProfile() {
        val userId = auth.currentUser?.uid ?: return
        db.child("users").child(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                _user.value = user
            }
            .addOnFailureListener {
                _user.value = null
            }
    }

    // Fungsi untuk memperbarui nama pengguna
    fun updateUserName(newName: String, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val userMap = mapOf("name" to newName)

        db.child("users").child(userId).updateChildren(userMap)
            .addOnSuccessListener {
                onComplete(true)  // Pembaruan berhasil
            }
            .addOnFailureListener {
                onComplete(false)  // Pembaruan gagal
            }
    }

    // Fungsi untuk memperbarui email pengguna
    fun updateUserEmail(newEmail: String, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val userMap = mapOf("email" to newEmail)

        db.child("users").child(userId).updateChildren(userMap)
            .addOnSuccessListener {
                onComplete(true)  // Pembaruan berhasil
            }
            .addOnFailureListener {
                onComplete(false)  // Pembaruan gagal
            }
    }
}
