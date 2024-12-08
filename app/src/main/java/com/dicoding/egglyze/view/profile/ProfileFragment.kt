package com.dicoding.egglyze.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.dicoding.egglyze.databinding.FragmentProfileBinding
import com.dicoding.egglyze.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Mengamati perubahan data profil
        profileViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            binding.profileName.text = profile?.name ?: "Nama Tidak Tersedia"
            binding.profileEmail.text = profile?.email ?: "Email Tidak Tersedia"
        }

        // Muat profil pengguna
        profileViewModel.loadUserProfile()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
