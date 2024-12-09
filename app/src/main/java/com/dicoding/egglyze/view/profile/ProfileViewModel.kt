package com.dicoding.egglyze.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.egglyze.view.profile.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> get() = _text

    private val _user = MutableLiveData<User?>()
    val user: MutableLiveData<User?> get() = _user

    fun loadUserProfile() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    _user.value = user
                    _text.value = user?.name ?: "Nama Tidak Tersedia"
                }
            }
            .addOnFailureListener {
                _user.value = null
                _text.value = "Gagal memuat data"
            }
    }
}
