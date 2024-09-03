package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.data.MediaRepository
import com.example.playlistmaker.media.data.impl.MediaRepositoryImpl
import org.koin.dsl.module

val mediaRepositoryModule = module {
    single<MediaRepository> {
        MediaRepositoryImpl(get(), get())
    }
}