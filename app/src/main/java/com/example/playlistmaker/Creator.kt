package com.example.playlistmaker

import com.example.playlistmaker.doamin.impl.MediaPlayRepImpl

object Creator {
    fun getMediaPlay(run: Runnable, url: String):MediaPlayRepImpl{
        return MediaPlayRepImpl(url, run)
    }
}