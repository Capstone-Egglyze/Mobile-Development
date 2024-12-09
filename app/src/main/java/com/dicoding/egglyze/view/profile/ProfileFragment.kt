package com.dicoding.egglyze.view.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dicoding.egglyze.databinding.FragmentProfileBinding
import com.dicoding.egglyze.view.auth.LoginActivity
import com.dicoding.egglyze.viewmodel.ProfileViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Listener untuk tombol logout
        binding.logoutSection.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Mengamati perubahan data profil
        profileViewModel.user.observe(viewLifecycleOwner) { profile ->
            Log.d("ProfileFragment", "Profile: ${profile?.name}, ${profile?.email}")  // Log data
            binding.profileName.text = profile?.name ?: "Nama Tidak Tersedia"
            binding.profileEmail.text = profile?.email ?: "Email Tidak Tersedia"
        }

        // Muat profil pengguna
        profileViewModel.loadUserProfile()

        return binding.root
    }

    private fun showLogoutConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin logout?")
            .setPositiveButton("Ya") { _, _ ->
                // Melakukan logout Firebase
                Firebase.auth.signOut()

                // Membuka LoginActivity dan menghapus aktivitas sebelumnya
                val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss() // Menutup dialog tanpa melakukan apapun
            }
            .create()

        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
