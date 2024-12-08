package com.dicoding.egglyze.view.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import com.dicoding.egglyze.R

class AnimationEgg : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home) // Ganti dengan layout Anda

        val eggImageView = findViewById<ImageView>(R.id.img_egg)

        // Buat animasi loncat
        val bounceUp = ObjectAnimator.ofFloat(eggImageView, "translationY", 0f, -50f).apply {
            duration = 300 // Waktu ke atas (ms)
            interpolator = AccelerateDecelerateInterpolator()
        }

        val bounceDown = ObjectAnimator.ofFloat(eggImageView, "translationY", -50f, 0f).apply {
            duration = 300 // Waktu turun (ms)
            interpolator = AccelerateDecelerateInterpolator()
        }

        // Gabungkan animasi
        val bounceAnimatorSet = AnimatorSet().apply {
            playSequentially(bounceUp, bounceDown)
        }

        // Loop animasi
        bounceAnimatorSet.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}
            override fun onAnimationEnd(animation: android.animation.Animator) {
                // Mulai ulang animasi setelah selesai
                bounceAnimatorSet.start()
            }
            override fun onAnimationCancel(animation: android.animation.Animator) {}
            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })

        // Mulai animasi
        bounceAnimatorSet.start()
    }
}
