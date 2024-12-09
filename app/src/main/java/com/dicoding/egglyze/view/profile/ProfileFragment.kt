package com.dicoding.egglyze.view.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dicoding.egglyze.databinding.FragmentProfileBinding
import com.dicoding.egglyze.view.auth.LoginActivity
import com.dicoding.egglyze.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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

        // Listener untuk tombol edit nama
        binding.imageButton.setOnClickListener {
            toggleEditName()
        }

        // Listener untuk tombol edit email
        binding.imageEmail.setOnClickListener {
            toggleEditEmail()
        }

        // Listener untuk tombol save nama
        binding.saveNameButton.setOnClickListener {
            val newName = binding.editName.text.toString()
            profileViewModel.updateUserName(newName) { success ->
                if (success) {
                    binding.profileName.text = newName
                    toggleEditName() // Menyembunyikan EditText setelah berhasil
                } else {
                    // Menampilkan error
                    Toast.makeText(requireContext(), "Gagal memperbarui nama", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Listener untuk tombol save email
        binding.saveEmailButton.setOnClickListener {
            val newEmail = binding.editEmail.text.toString()
            profileViewModel.updateUserEmail(newEmail) { success ->
                if (success) {
                    binding.profileEmail.text = newEmail
                    toggleEditEmail() // Menyembunyikan EditText setelah berhasil
                } else {
                    // Menampilkan error
                    Toast.makeText(requireContext(), "Gagal memperbarui email", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.logoutSection.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Mengamati perubahan data profil
        profileViewModel.user.observe(viewLifecycleOwner) { profile ->
            binding.profileName.text = profile?.name ?: "Nama Tidak Tersedia"
            binding.profileEmail.text = profile?.email ?: "Email Tidak Tersedia"
        }

        // Muat profil pengguna
        profileViewModel.loadUserProfile()

        return binding.root
    }

    // Fungsi untuk menampilkan dan menyembunyikan EditText untuk nama
    private fun toggleEditName() {
        val isVisible = binding.editName.visibility == View.VISIBLE
        binding.editName.visibility = if (isVisible) View.GONE else View.VISIBLE
        binding.saveNameButton.visibility = if (isVisible) View.GONE else View.VISIBLE

        if (!isVisible) {
            binding.editName.setText(binding.profileName.text)
        }
    }

    // Fungsi untuk menampilkan dan menyembunyikan EditText untuk email
    private fun toggleEditEmail() {
        val isVisible = binding.editEmail.visibility == View.VISIBLE
        binding.editEmail.visibility = if (isVisible) View.GONE else View.VISIBLE
        binding.saveEmailButton.visibility = if (isVisible) View.GONE else View.VISIBLE

        if (!isVisible) {
            binding.editEmail.setText(binding.profileEmail.text)
        }
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
