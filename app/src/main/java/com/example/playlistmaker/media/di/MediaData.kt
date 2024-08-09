package com.example.playlistmaker.media.di

import androidx.room.Room
import com.example.playlistmaker.media.data.converters.TrackConvertor
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.dao.TrackDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mediaDataModule = module{
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    factory{
        TrackConvertor()
    }

}