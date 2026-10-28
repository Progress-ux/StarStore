package com.progress.starstoreclient

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StarStoreApi {
    data class Model(
        val id: Long = 0L,
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

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("name") name: String,
        @Field("pass") pass: String
    )

    @GET("money/{name}")
    suspend fun money(@Path("name") name: String = user): Long

    @GET("faves/{name}")
    suspend fun faves(@Path("name") name: String = user): List<Long>

    @GET("cart/{name}")
    suspend fun cartModels(@Path("name") name: String = user): List<Long>

    @FormUrlEncoded
    @POST("fav/{id}")
    suspend fun fav(
        @Path("id") id: Long,
        @Field("name") name: String = user,
        @Field("pass") password: String = pass
    )

    @GET("cart/{name}/{id}")
    suspend fun cart(
        @Path("id") id: Long,
        @Path("name") name: String = user
    ): Long

    @FormUrlEncoded
    @POST("cart/{id}")
    suspend fun add(
        @Path("id") id: Long,
        @Field("amount") amount: Long,
        @Field("name") name: String = user,
        @Field("pass") password: String = pass,
    )


    companion object {
        private const val URL = "http://192.168.100.2:8080/"
        const val IMAGES = URL + "images/"

        var user by mutableStateOf("")
        var pass by mutableStateOf("")

        fun getApi(): StarStoreApi = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create()
    }
}