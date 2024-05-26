package com.example.playlistmaker.creator

import com.example.playlistmaker.player.domain.impl.MediaPlayRepImpl

object Creator {
    fun getMediaPlay(run: Runnable, url: String): MediaPlayRepImpl {
        return MediaPlayRepImpl(url, run)
    }
}