package com.dicoding.egglyze.view.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.room.Database
import com.dicoding.egglyze.data.database.DatabaseDao
import com.dicoding.egglyze.data.database.MyDatabase
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseDao: DatabaseDao = MyDatabase.getDatabase(application).databaseDao()

    // LiveData<List<History>> that will be observed by the UI
    private val _historys = MutableLiveData<List<History>>()
    val history: LiveData<List<History>> get() = _historys

    init {
        loadHistory()
    }

    private fun loadHistory() {
        // Mengambil data dari Room Database
        viewModelScope.launch {
            // Mengambil data dari DAO yang berupa LiveData<List<HistoryEntity>>
            val historyEntities = databaseDao.getAllHistory() // LiveData<List<HistoryEntity>>

            // Observe the LiveData and transform the data once it's available
            historyEntities.observeForever { historyList ->
                // Transform List<HistoryEntity> to List<History>
                val historyItems = historyList.map {
                    History(
                        it.id,
                        it.image,
                        it.predictAt,
                        it.result,
                        it.confidence,
                        it.predictedClass
                    )
                }
                // Update the MutableLiveData with the transformed list
                _historys.value = historyItems
            }
        }
    }
}



