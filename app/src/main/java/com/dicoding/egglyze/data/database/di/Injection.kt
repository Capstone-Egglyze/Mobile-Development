package com.dicoding.egglyze.data.database.di

import android.content.Context
import androidx.room.Database
import com.dicoding.egglyze.data.database.MyDatabase

object Injection {

    fun provideDatabaseDao(context: Context) = MyDatabase.getDatabase(context).databaseDao()
}
