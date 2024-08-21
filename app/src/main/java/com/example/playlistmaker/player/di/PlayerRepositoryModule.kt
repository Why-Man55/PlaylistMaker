package com.example.playlistmaker.player.di

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.player.data.MediaPlayRepository
import com.example.playlistmaker.player.data.impl.MediaPlayRepImpl
import org.koin.dsl.module

val playerRepositoryModule = module{
    single{
        MediaPlayer()
    }

    single<MediaPlayRepository> {
        MediaPlayRepImpl(get())
    }
}