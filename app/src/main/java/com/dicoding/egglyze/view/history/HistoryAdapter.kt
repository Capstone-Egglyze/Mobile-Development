package com.dicoding.egglyze.view.history

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.egglyze.databinding.ItemHistoryBinding
import com.bumptech.glide.Glide
import com.dicoding.egglyze.view.camera.ResultActivity.Companion.EXTRA_IMAGE_URI

class HistoryAdapter(private val historyList: List<History>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    // ViewHolder untuk item dalam RecyclerView
    class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            Log.d("HistoryAdapter", "Loading image: ${history.image}")
            binding.tvHistory.text = history.confidence
            binding.tvDate.text = history.predictedClass

            if (history.image.startsWith("content://")) {
                val imageUri = Uri.parse(history.image)
                try {
                    Glide.with(binding.imgHistory.context)
                        .load(imageUri)
                        .into(binding.imgHistory)
                } catch (e: SecurityException) {
                    Log.e("HistoryAdapter", "Permission issue with URI: $imageUri", e)
                }
            } else {
                Glide.with(binding.imgHistory.context)
                    .load(history.image)
                    .into(binding.imgHistory)
            }

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

