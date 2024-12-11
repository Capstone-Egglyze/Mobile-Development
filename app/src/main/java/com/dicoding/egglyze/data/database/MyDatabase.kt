package com.dicoding.egglyze.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabaseEntity::class], version = 2, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    abstract fun databaseDao(): DatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: MyDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): MyDatabase {
            if (INSTANCE == null) {
                synchronized(MyDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "history_database"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE as MyDatabase
        }
    }
}