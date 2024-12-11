package com.dicoding.egglyze.view.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.egglyze.databinding.ItemHistoryBinding
import com.bumptech.glide.Glide

class HistoryAdapter(private val historyList: List<History>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    // ViewHolder untuk item dalam RecyclerView
    class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            binding.tvHistory.text = history.confidence
            binding.tvDate.text = history.predictedClass

            // Memuat gambar menggunakan Glide jika history.image adalah URL
            Glide.with(binding.imgHistory.context)
                .load(history.image) // Menggunakan string URL
                .into(binding.imgHistory) // Menetapkan gambar ke ImageView
        }
    }

    // Membuat ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    // Mengikat data ke ViewHolder
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = historyList[position]
        holder.bind(history)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}

