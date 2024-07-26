package com.example.playlistmaker.player.data

import android.media.MediaPlayer


interface MediaPlayRepository {
    fun getReadyMedia(url: String?)
    fun startPlayer()
    fun pausePlayer()
    fun returnCurrentPosition(): Int
    fun playRelease()
    fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener)
    fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener)
}