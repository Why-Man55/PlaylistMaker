package com.example.playlistmaker.creator

import android.app.Application
import android.content.SharedPreferences
import com.example.playlistmaker.player.data.dto.MediaPlayRepImpl
import com.example.playlistmaker.player.domain.api.MediaPlayRepository
import com.example.playlistmaker.search.data.dto.RetrofitControllerRepImpl
import com.example.playlistmaker.search.data.dto.SearchHistoryRepImpl
import com.example.playlistmaker.search.domain.api.RetrofitControllerRepository
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.settings.data.dto.ThemeSaveRepImpl
import com.example.playlistmaker.settings.domain.api.ThemeSaveRepository

object Creator {
    lateinit var application: Application

    fun getMediaPlay(url: String, run: Runnable): MediaPlayRepository {
        return MediaPlayRepImpl(url, run)
    }
    fun getSearchHistory(sp:SharedPreferences): SearchHistoryRepository{
        return SearchHistoryRepImpl(sp)
    }
    fun getThemeSave(sp:SharedPreferences): ThemeSaveRepository {
        return ThemeSaveRepImpl(sp)
    }
    fun getRetrofitController(): RetrofitControllerRepository {
        return RetrofitControllerRepImpl()
    }
    fun initApplication(app: Application){
        application = app
    }
}