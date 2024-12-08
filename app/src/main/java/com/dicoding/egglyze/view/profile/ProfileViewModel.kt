package com.dicoding.egglyze.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.egglyze.view.profile.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> get() = _text

    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: MutableLiveData<UserProfile?> get() = _userProfile

    fun loadUserProfile() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userProfile = document.toObject(UserProfile::class.java)
                    _userProfile.value = userProfile
                    _text.value = userProfile?.name ?: "Nama Tidak Tersedia"
                }
            }
            .addOnFailureListener {
                _userProfile.value = null
                _text.value = "Gagal memuat data"
            }
    }
}
