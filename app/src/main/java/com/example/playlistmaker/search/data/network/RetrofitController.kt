package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitController: NetworkClient{
    private val application = Creator.application

    private val retrofit = Retrofit.Builder()
        .baseUrl(application.getString(R.string.iTunes))
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    override fun doSearch(dto: Any): Response {
        return if (dto is TrackSearchRequest) {
            val resp = iTunesService.search(dto.expression).execute()

            val body = resp.body() ?: Response()

            body.apply { searchState = resp.code() }
        } else {
            Response().apply { searchState = 400 }
        }
    }
}