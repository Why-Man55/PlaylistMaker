package com.example.playlistmaker.search.domain.api

import retrofit2.Retrofit

interface RetrofitControllerRepository {
    fun createRetrofit(): Retrofit
}