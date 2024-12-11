package com.dicoding.egglyze.view.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dicoding.egglyze.databinding.FragmentProfileBinding
import com.dicoding.egglyze.view.auth.LoginActivity
import com.dicoding.egglyze.view.setting.SettingActivity
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

        setupListeners()
        observeViewModel()

        // Muat profil pengguna
        profileViewModel.loadUserProfile()

        return binding.root
    }

    private fun setupListeners() {
        // Listener untuk tombol edit nama
        binding.imgArrowName.setOnClickListener {
            toggleEditVisibility(
                editText = binding.editName,
                saveButton = binding.saveNameButton,
                textView = binding.profileName
            )
        }

        // Listener untuk tombol edit email
        binding.imgArrowEmail.setOnClickListener {
            toggleEditVisibility(
                editText = binding.editEmail,
                saveButton = binding.saveEmailButton,
                textView = binding.profileEmail
            )
        }

        // Listener untuk tombol save nama
        binding.saveNameButton.setOnClickListener {
            val newName = binding.editName.text.toString()
            if (newName.isNotEmpty()) {
                profileViewModel.updateUserName(newName) { success ->
                    if (success) {
                        binding.profileName.text = newName
                        toggleEditVisibility(
                            editText = binding.editName,
                            saveButton = binding.saveNameButton,
                            textView = binding.profileName
                        )
                    } else {
                        Toast.makeText(requireContext(), "Gagal memperbarui nama", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Listener untuk tombol save email
        binding.saveEmailButton.setOnClickListener {
            val newEmail = binding.editEmail.text.toString()
            if (newEmail.isNotEmpty()) {
                profileViewModel.updateUserEmail(newEmail) { success ->
                    if (success) {
                        binding.profileEmail.text = newEmail
                        toggleEditVisibility(
                            editText = binding.editEmail,
                            saveButton = binding.saveEmailButton,
                            textView = binding.profileEmail
                        )
                    } else {
                        Toast.makeText(requireContext(), "Gagal memperbarui email", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Listener untuk logout
        binding.logoutSection.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Listener untuk membuka Setting Activity
        binding.themeSection.setOnClickListener {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        profileViewModel.user.observe(viewLifecycleOwner) { profile ->
            binding.profileName.text = profile?.name ?: "Nama Tidak Tersedia"
            binding.profileEmail.text = profile?.email ?: "Email Tidak Tersedia"
        }
    }

    private fun toggleEditVisibility(editText: View, saveButton: View, textView: View) {
        val isVisible = editText.visibility == View.VISIBLE
        editText.visibility = if (isVisible) View.GONE else View.VISIBLE
        saveButton.visibility = if (isVisible) View.GONE else View.VISIBLE
        textView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showLogoutConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin logout?")
            .setPositiveButton("Ya") { _, _ ->
                Firebase.auth.signOut()

                val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
