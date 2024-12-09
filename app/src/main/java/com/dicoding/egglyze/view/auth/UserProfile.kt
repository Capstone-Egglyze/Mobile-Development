package com.dicoding.egglyze.view.auth

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val phoneNumber: String? = null,  // Jika ingin menambahkan informasi lain
    val profilePictureUrl: String? = null  // Jika ingin menambahkan URL gambar profil
)
