package com.dicoding.egglyze.view.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AlertDialog
import com.dicoding.egglyze.R
import com.dicoding.egglyze.databinding.FragmentHomeBinding
import com.dicoding.egglyze.view.camera.CameraGalleryActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Menampilkan tanggal di TextView
        val dateTextView = binding.dateTextView

        // Mendapatkan tanggal saat ini
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        // Menampilkan tanggal di TextView
        dateTextView.text = currentDate

        // Update text jika menggunakan ViewModel
        val textView = binding.toolbarTitle
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Mengatur klik pada scan_img untuk membuka CameraGalleryActivity
        binding.scanImg.setOnClickListener {
            val intent = Intent(activity, CameraGalleryActivity::class.java)
            startActivity(intent)
        }

        // Mengatur klik pada buttonInfo untuk menampilkan AlertDialog
        binding.buttonInfo.setOnClickListener {
            showInfoDialog()
        }
        binding.linearMain.setOnClickListener {
            // Membuka PremiumActivity ketika linear_main diklik
            val intent = Intent(activity, PremiumActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    private fun showInfoDialog() {
        // Membuat custom AlertDialog dengan animasi zoom in dan zoom out
        val dialogView = layoutInflater.inflate(R.layout.dialog_info, null)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true) // Agar dialog bisa ditutup dengan klik luar

        val dialog = dialogBuilder.create()

        // Mengambil View dari dialog untuk animasi
        val dialogContent = dialogView.findViewById<View>(R.id.dialogContent)

        // Animasi zoom in saat dialog muncul
        dialog.window?.setWindowAnimations(android.R.style.Animation_Dialog) // Menambahkan animasi standar
        dialog.show()

        val zoomIn = ObjectAnimator.ofFloat(dialogContent, "scaleX", 0f, 1f).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
        }

        val zoomInY = ObjectAnimator.ofFloat(dialogContent, "scaleY", 0f, 1f).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
        }

        // Memulai animasi zoom in
        zoomIn.start()
        zoomInY.start()

        // Menutup dialog dengan animasi zoom out setelah beberapa detik
        dialog.setOnDismissListener {
            val zoomOut = ObjectAnimator.ofFloat(dialogContent, "scaleX", 1f, 0f).apply {
                duration = 300
                interpolator = AccelerateDecelerateInterpolator()
            }

            val zoomOutY = ObjectAnimator.ofFloat(dialogContent, "scaleY", 1f, 0f).apply {
                duration = 300
                interpolator = AccelerateDecelerateInterpolator()
            }

            // Memulai animasi zoom out
            zoomOut.start()
            zoomOutY.start()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tambahkan animasi untuk ImageView (telur)
        val eggImageView: ImageView = binding.imgEgg

        val bounceUp = ObjectAnimator.ofFloat(eggImageView, "translationY", 0f, -50f).apply {
            duration = 300 // Waktu ke atas
            interpolator = AccelerateDecelerateInterpolator()
        }

        val bounceDown = ObjectAnimator.ofFloat(eggImageView, "translationY", -50f, 0f).apply {
            duration = 300 // Waktu turun
            interpolator = AccelerateDecelerateInterpolator()
        }

        val bounceAnimatorSet = AnimatorSet().apply {
            playSequentially(bounceUp, bounceDown)
            addListener(object : android.animation.Animator.AnimatorListener {
                override fun onAnimationStart(animation: android.animation.Animator) {}
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    start() // Loop animasi
                }
                override fun onAnimationCancel(animation: android.animation.Animator) {}
                override fun onAnimationRepeat(animation: android.animation.Animator) {}
            })
        }

        bounceAnimatorSet.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}