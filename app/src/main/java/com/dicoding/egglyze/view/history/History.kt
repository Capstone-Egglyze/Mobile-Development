package com.dicoding.egglyze.view.history

data class History(
    val id: Int,
    val image: String,  // Sesuai dengan tipe data di HistoryEntity
    val predictAt: String,
    val result: String,
    val confidence: String,
    val predictedClass: String
)

