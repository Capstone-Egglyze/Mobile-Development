package com.dicoding.egglyze.data.remote.response

import com.google.gson.annotations.SerializedName

data class EggResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("prediction")
    val prediction: Prediction? = null
)

data class Prediction(

    @field:SerializedName("confidence")
    val confidence: String? = null,

    @field:SerializedName("predicted_class")
    val predictedClass: String? = null
)
