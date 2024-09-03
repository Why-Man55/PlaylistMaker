package com.example.playlistmaker.settings.di

import android.content.Context
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsRepositoryModule = module {
    single { androidContext().getSharedPreferences("key_pretext", Context.MODE_PRIVATE) }
    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}