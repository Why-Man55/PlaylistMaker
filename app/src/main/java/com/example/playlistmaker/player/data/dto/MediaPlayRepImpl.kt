package com.example.playlistmaker.player.data.dto

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

class MediaPlayRepImpl(private val url: String, private val run: Runnable) {
    val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())

    fun getReadyMedia(){
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    fun handlerPostDelayed(time: Long){
        handler.postDelayed(run, time)
    }

    fun handlerPost(){
        handler.post(run)
    }

    fun handlerCallBack(){
        handler.removeCallbacks(run)
    }

    fun startPlayer() {
        mediaPlayer.start()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
    }

    fun returnCurrentPosition(): Int{
        return mediaPlayer.currentPosition
    }

    fun playRelease(){
        mediaPlayer.release()
    }
}