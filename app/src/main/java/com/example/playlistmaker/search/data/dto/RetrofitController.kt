package com.example.playlistmaker.search.data.dto

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitController {
    fun createRetrofit(baseUrl: String):Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}