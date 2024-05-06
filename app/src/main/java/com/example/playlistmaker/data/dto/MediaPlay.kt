package com.example.playlistmaker.data.dto

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

class MediaPlay(private val url: String) {
    val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())

    fun getReadyMedia(){
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    fun handlerPostDelayed(run: Runnable, time: Long){
        handler.postDelayed(run, time)
    }

    fun handlerPost(run: Runnable){
        handler.post(run)
    }

    fun handlerCallBack(run: Runnable){
        handler.removeCallbacks(run)
    }

    fun startPlayer() {
        mediaPlayer.start()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
    }

    fun returnCurrentPosition(): Int{
        return  mediaPlayer.currentPosition
    }

    fun playRelease(){
        mediaPlayer.release()
    }
}