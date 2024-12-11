package com.dicoding.egglyze.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(databaseEntity: DatabaseEntity)

    @Query("DELETE FROM history_table WHERE predictAt = :predictAt")
    suspend fun delete(predictAt: String?)

    @Query("SELECT * from history_table ORDER BY id DESC")
    fun getAllHistory(): LiveData<List<DatabaseEntity>>

    @Query("SELECT COUNT(*) FROM history_table WHERE predictAt = :predictAt")
    fun checkHistory(predictAt: String?): LiveData<Int>

}