package com.example.playlistmaker.player.domain

import android.media.MediaPlayer

interface PlayerInteractor {
    fun getReadyMedia(url:String?)
    fun startPlayer()
    fun pausePlayer()
    fun returnCurrentPosition(): Int
    fun playRelease()
    fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener)
    fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener)
}