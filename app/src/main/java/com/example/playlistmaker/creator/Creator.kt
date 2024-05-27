package com.example.playlistmaker.creator

import android.content.SharedPreferences
import com.example.playlistmaker.player.domain.impl.MediaPlayRepImpl
import com.example.playlistmaker.search.domain.impl.SearchHistoryReplmpl

object Creator {
    fun getMediaPlay(run: Runnable, url: String): MediaPlayRepImpl {
        return MediaPlayRepImpl(url, run)
    }
    fun getSearchHistory(sp:SharedPreferences):SearchHistoryReplmpl{
        return SearchHistoryReplmpl(sp)
    }
}