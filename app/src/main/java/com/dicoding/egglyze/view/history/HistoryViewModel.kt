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
    private val _historys: LiveData<List<History>> = databaseDao.getAllHistory().map { entities ->
        entities.map {
            History(
                it.id,
                it.image,
                it.predictAt,
                it.result,
                it.confidence,
                it.predictedClass
            )
        }
    }
    val history: LiveData<List<History>> get() = _historys

}




