package com.example.playlistmaker.search.data.dto

import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.api.RetrofitControllerRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitControllerRepImpl: RetrofitControllerRepository {
    override fun createRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl(App().getString(R.string.iTunes))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}