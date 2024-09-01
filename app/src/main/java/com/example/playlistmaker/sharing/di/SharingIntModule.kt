package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingIntractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharingIntModule = module {
    single<SharingInteractor> {
        SharingIntractorImpl(get(), androidContext())
    }
}