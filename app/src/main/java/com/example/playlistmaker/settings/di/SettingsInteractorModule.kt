package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.domain.SettingInteractor
import com.example.playlistmaker.settings.domain.impl.SettingInteractorImpl
import org.koin.dsl.module

val settingsaIntModule = module {
    single<SettingInteractor> {
        SettingInteractorImpl(get())
    }
}