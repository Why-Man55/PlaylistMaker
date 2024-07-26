package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.SearchHistoryRepository
import com.example.playlistmaker.search.data.TrackRepository
import com.example.playlistmaker.search.data.impl.SearchHistoryRepImpl
import com.example.playlistmaker.search.data.impl.TrackRepositoryImpl
import org.koin.dsl.module

val searchRepositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepImpl(get())
    }

}