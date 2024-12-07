package com.dicoding.egglyze.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.egglyze.R
import com.dicoding.egglyze.databinding.ActivityMainBinding
import com.dicoding.egglyze.view.auth.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cek status login user
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            // Jika user belum login, arahkan ke LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        } else {
            Log.d("MainActivity", "User logged in: ${currentUser.email}")
        }

        // Listener untuk tombol logout
        binding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set up Bottom Navigation View
        val navView: BottomNavigationView = binding.navView

        // Mendapatkan NavController dari NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Tentukan destinasi top-level untuk navigasi
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        )

        // Set up ActionBar dengan NavController
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Set up BottomNavigationView dengan NavController
        navView.setupWithNavController(navController)

        // Handle pemilihan item pada Bottom Navigation
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Tindakan untuk Home
                    Log.d("MainActivity", "Home clicked")
                    true
                }
                R.id.navigation_dashboard -> {
                    // Tindakan untuk Dashboard
                    Log.d("MainActivity", "Dashboard clicked")
                    true
                }
                R.id.navigation_notifications -> {
                    // Tindakan untuk Notifications
                    Log.d("MainActivity", "Notifications clicked")
                    true
                }
                else -> false
            }
        }

        // Set item default yang dipilih di Bottom Navigation
        if (savedInstanceState == null) {
            navView.selectedItemId = R.id.navigation_home
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        Log.d("MainActivity", "Current User: ${currentUser?.email ?: "No user"}")
    }
}