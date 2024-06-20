package com.example.playlistmaker.search.di

import android.content.Context
import com.example.playlistmaker.search.data.HandlerControllerRep
import com.example.playlistmaker.search.data.HistoryControlRepository
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.impl.HandlerControllerRepimpl
import com.example.playlistmaker.search.data.dto.HistoryController
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.search.data.network.RetrofitController
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchDataModule = module {
    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single {
        androidContext().getSharedPreferences("key_for_historySP", Context.MODE_PRIVATE)
    }
    factory { Gson() }

    single<HistoryControlRepository> {
        HistoryController(get())
    }

    single<NetworkClient> {
        RetrofitController(get(), androidContext())
    }

    single<HandlerControllerRep> {
        HandlerControllerRepimpl()
    }
}