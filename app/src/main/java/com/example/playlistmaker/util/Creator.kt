package com.example.playlistmaker.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.player.data.impl.MediaPlayRepImpl
import com.example.playlistmaker.player.data.MediaPlayRepository
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitController
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.data.TrackRepository
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingInteractor
import com.example.playlistmaker.settings.domain.impl.SettingInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigatorRepository
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorRepImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingIntractorImpl

object Creator {
    lateinit var application: Application
    private fun getTrackRepository(sp:SharedPreferences,context: Context): TrackRepository {
        return TrackRepositoryImpl(sp, RetrofitController(context))
    }

    fun provideTrackInteractor(sp:SharedPreferences,context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(sp,context))
    }

    private fun getSettingRepository(sp:SharedPreferences): SettingsRepository {
        return SettingsRepositoryImpl(sp)
    }

    fun provideSettingsInteractor(sp:SharedPreferences): SettingInteractor {
        return SettingInteractorImpl(getSettingRepository(sp))
    }

    private fun getPlayerRepository(url: String, run: Runnable): MediaPlayRepository{
        return MediaPlayRepImpl(url,run)
    }

    fun providePlayerInteractor(url: String, run: Runnable): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository(url,run))
    }
    fun initApplication(app: Application){
        application = app
    }
    fun getSharingInt(context: Context):SharingInteractor{
        return SharingIntractorImpl(getNavigator(context))
    }
    private fun getNavigator(context: Context):ExternalNavigatorRepository{
        return ExternalNavigatorRepImpl(context)
    }
}