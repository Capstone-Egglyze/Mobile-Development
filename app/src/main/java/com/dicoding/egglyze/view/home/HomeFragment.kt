package com.dicoding.egglyze.view.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.egglyze.databinding.FragmentHomeBinding

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

        // Update text jika menggunakan ViewModel
        val textView = binding.toolbarTitle
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
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
