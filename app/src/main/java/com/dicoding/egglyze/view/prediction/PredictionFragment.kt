package com.example.yourapp.view.prediction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dicoding.egglyze.R

class PredictionFragment : Fragment() {

    private lateinit var tvResult: TextView
    private lateinit var btnPredict: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_prediction, container, false)

        // Inisialisasi elemen tampilan
        tvResult = rootView.findViewById(R.id.tv_start_prediction)
        btnPredict = rootView.findViewById(R.id.btn_start_prediction)

        // Set listener untuk tombol prediksi
        btnPredict.setOnClickListener {
            // Simulasikan prediksi atau panggil logika prediksi
            predictResult()
        }

        return rootView
    }

    private fun predictResult() {
        // Contoh logika prediksi (misalnya, menampilkan hasil acak atau nilai tertentu)
        val prediction = "Hasil Prediksi: Positif"
        tvResult.text = prediction
    }
}
