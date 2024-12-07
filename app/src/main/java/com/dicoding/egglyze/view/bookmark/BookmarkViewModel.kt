package com.dicoding.egglyze.view.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.egglyze.R

class BookmarkViewModel : ViewModel() {

    private val _bookmarks = MutableLiveData<List<Bookmark>>()
    val bookmarks: LiveData<List<Bookmark>> get() = _bookmarks

    // Menyediakan data statis untuk bookmark
    init {
        // Data bookmark dummy
        _bookmarks.value = listOf(
            Bookmark("Telur Ayam", "Aug 15, 2024", R.drawable.egg),
            Bookmark("Telur Bebek", "Sep 22, 2024", R.drawable.egg),
            Bookmark("Telur Puyuh", "Nov 5, 2024", R.drawable.egg)
        )
    }
}
