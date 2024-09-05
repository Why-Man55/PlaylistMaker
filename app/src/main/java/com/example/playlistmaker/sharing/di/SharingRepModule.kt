package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.data.ExternalNavigatorRepository
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorRepImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharingsRepModule = module {
    single<ExternalNavigatorRepository> {
        ExternalNavigatorRepImpl(androidContext())
    }
}