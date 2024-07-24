package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl
import org.koin.dsl.module

val searchInteractorModule = module {
    single<TrackInteractor>{
        TrackInteractorImpl(get())
    }

    single<SearchHistoryInteractor>{
        SearchHistoryInteractorImpl(get())
    }
}