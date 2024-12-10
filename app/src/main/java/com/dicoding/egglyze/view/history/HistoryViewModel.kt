package com.dicoding.egglyze.view.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.egglyze.R

class HistoryViewModel : ViewModel() {

    private val _historys = MutableLiveData<List<History>>()
    val history: LiveData<List<History>> get() = _historys

    // Menyediakan data statis untuk History
    init {
        // Data History dummy
        _historys.value = listOf(
            History("Telur Ayam", "Aug 15, 2024", R.drawable.egg),
            History("Telur Ayam", "Sep 22, 2024", R.drawable.egg),
            History("Telur Ayam", "Nov 5, 2024", R.drawable.egg),
            History("Telur Ayam", "Aug 15, 2024", R.drawable.egg),
            History("Telur Ayam", "Sep 22, 2024", R.drawable.egg),
            History("Telur Ayam", "Nov 5, 2024", R.drawable.egg),
            History("Telur Ayam", "Aug 15, 2024", R.drawable.egg),
            History("Telur Ayam", "Sep 22, 2024", R.drawable.egg),
            History("Telur Ayam", "Nov 5, 2024", R.drawable.egg)
        )
    }
}
