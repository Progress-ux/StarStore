package com.progress.starstoreclient

interface StarStoreApi {
    data class Model(
        val id: Int,
        val name: String = "",
        val price: Long = 0L,
        val image: String? = null,
        val ship: Int = 0,
        val info: String? = null
    )
}