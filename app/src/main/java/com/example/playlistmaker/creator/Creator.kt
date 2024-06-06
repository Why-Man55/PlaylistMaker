package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.player.data.dto.MediaPlayRepImpl
import com.example.playlistmaker.player.domain.api.MediaPlayRepository
import com.example.playlistmaker.search.data.SearchHistoryRepImpl
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitController
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.settings.domain.SettingInteractor
import com.example.playlistmaker.settings.domain.impl.SettingInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigatorRepository
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorRepImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingIntractorImpl

object Creator {
    lateinit var application: Application

    fun getMediaPlay(url: String, run: Runnable): MediaPlayRepository {
        return MediaPlayRepImpl(url, run)
    }
    fun getSearchHistory(sp:SharedPreferences): SearchHistoryRepository{
        return SearchHistoryRepImpl(sp)
    }
    private fun getMoviesRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitController())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getMoviesRepository())
    }
    fun initApplication(app: Application){
        application = app
    }
    fun getSettingsInt(sp:SharedPreferences): SettingInteractor {
        return SettingInteractorImpl(sp)
    }
    fun getSharingInt(context: Context):SharingInteractor{
        return SharingIntractorImpl(getNavigator(context))
    }
    private fun getNavigator(context: Context):ExternalNavigatorRepository{
        return ExternalNavigatorRepImpl(context,application)
    }
}