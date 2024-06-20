package com.example.playlistmaker.player.data

import android.media.MediaPlayer


interface MediaPlayRepository {
    fun getReadyMedia(url: String?)
    fun handlerPostDelayed(run: Runnable,time: Long)
    fun handlerPost(run: Runnable)
    fun handlerCallBack(run: Runnable)
    fun startPlayer()
    fun pausePlayer()
    fun returnCurrentPosition(): Int
    fun playRelease()
    fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener)
    fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener)
}