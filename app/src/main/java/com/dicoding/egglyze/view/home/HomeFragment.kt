package com.dicoding.egglyze.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.egglyze.databinding.FragmentHomeBinding
import com.dicoding.egglyze.view.camera.CameraGalleryActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.toolbarTitle
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Menangani klik pada ImageButton
        val scanButton: ImageView = binding.scanImg
        scanButton.setOnClickListener {
            // Berpindah ke CameraActivity
            val intent = Intent(requireContext(), CameraGalleryActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
