package com.dicoding.egglyze.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "history_table")
data class DatabaseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "predictAt")
    var predictAt: String,

    @ColumnInfo(name = "result")
    var result: String,

    @ColumnInfo(name = "confidence")
    var confidence: String,

    @ColumnInfo(name = "predictedClass")
    var predictedClass: String

)
