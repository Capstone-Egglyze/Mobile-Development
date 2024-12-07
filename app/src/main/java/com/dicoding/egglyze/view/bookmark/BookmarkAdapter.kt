package com.dicoding.egglyze.view.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.egglyze.databinding.ItemBookmarkBinding

class BookmarkAdapter(private val bookmarkList: List<Bookmark>) :
    RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    // ViewHolder untuk item dalam RecyclerView
    class BookmarkViewHolder(private val binding: ItemBookmarkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookmark: Bookmark) {
            binding.tvPlant.text = bookmark.title
            binding.tvDate.text = bookmark.date
            binding.imgPlant.setImageResource(bookmark.imageResId)
        }
    }

    // Membuat ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }

    // Mengikat data ke ViewHolder
    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val bookmark = bookmarkList[position]
        holder.bind(bookmark)
    }

    override fun getItemCount(): Int {
        return bookmarkList.size
    }
}
