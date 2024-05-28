package com.example.playlistmaker.creator

import android.content.SharedPreferences
import com.example.playlistmaker.player.data.dto.MediaPlayRepImpl
import com.example.playlistmaker.search.data.dto.RetrofitController
import com.example.playlistmaker.search.domain.impl.SearchHistoryReplmpl
import com.example.playlistmaker.settings.data.dto.ThemeSave

object Creator {
    fun getMediaPlay(run: Runnable, url: String): MediaPlayRepImpl {
        return MediaPlayRepImpl(url, run)
    }
    fun getSearchHistory(sp:SharedPreferences):SearchHistoryReplmpl{
        return SearchHistoryReplmpl(sp)
    }
    fun getThemeSave(sp: SharedPreferences): ThemeSave{
        return ThemeSave(sp)
    }
    fun getRetrofitController():RetrofitController{
        return RetrofitController()
    }
}