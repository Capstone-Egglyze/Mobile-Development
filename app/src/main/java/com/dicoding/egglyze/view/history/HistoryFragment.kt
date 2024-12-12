package com.dicoding.egglyze.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.egglyze.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inisialisasi ViewModel
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        // Menginflate binding untuk fragment
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inisialisasi RecyclerView dan Adapter
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())

        // Mengamati data di ViewModel
        historyViewModel.history.observe(viewLifecycleOwner) { historys ->
            // Pastikan data historys tidak null
            if (!historys.isNullOrEmpty()) {
                // Menyambungkan Adapter dengan data
                historyAdapter = HistoryAdapter(historys)
                binding.rvHistory.adapter = historyAdapter
            } else {
                // Tampilkan pesan jika tidak ada data
                showToast("No history available")
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}


