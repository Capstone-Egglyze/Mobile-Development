package com.dicoding.egglyze.view.auth

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User

class FirebaseDatabaseHelper {

    private val database = FirebaseDatabase.getInstance().reference

    // Fungsi untuk mengambil data pengguna dari Realtime Database
    fun getUserData(userId: String, onComplete: (UserProfile?) -> Unit) {
        database.child("userProfiles").child(userId).get()
            .addOnSuccessListener { snapshot ->
                val userProfile = snapshot.getValue(UserProfile::class.java)
                onComplete(userProfile)
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }
}