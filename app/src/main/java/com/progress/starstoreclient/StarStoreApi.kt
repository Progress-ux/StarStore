package com.progress.starstoreclient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path

interface StarStoreApi {
    data class Model(
        val id: Long,
        val name: String = "",
        val price: Long = 0L,
        val image: String? = null,
        val ship: Long = 0L,
        val info: String? = null
    )

    @GET("models")
    suspend fun models(): List<Model>

    @GET("model/{id}")
    suspend fun model(@Path("id") id: Long): Model

    companion object {
        private const val URL = "http://192.168.100.2:8080/"

        fun getApi(): StarStoreApi = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create()
    }
}