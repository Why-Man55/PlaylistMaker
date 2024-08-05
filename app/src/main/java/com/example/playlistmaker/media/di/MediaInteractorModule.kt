package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.domain.MediaInteractor
import com.example.playlistmaker.media.domain.impl.MediaInteractorImpl
import org.koin.dsl.module

val mediaInteractorModule = module{
    single<MediaInteractor>{
        MediaInteractorImpl(get())
    }
}