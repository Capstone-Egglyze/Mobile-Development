package com.dicoding.egglyze.data.remote.retrofit

import com.dicoding.egglyze.data.remote.response.EggResponse
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.MultipartBody

interface ApiService {

    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): EggResponse
}
