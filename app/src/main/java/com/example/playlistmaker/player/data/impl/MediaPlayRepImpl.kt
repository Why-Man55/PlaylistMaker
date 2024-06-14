package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.player.data.MediaPlayRepository

class MediaPlayRepImpl(private val mediaPlayer: MediaPlayer,  private val handler: Handler): MediaPlayRepository {

    override fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener) {
        mediaPlayer.setOnPreparedListener(listener)
    }

    override fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        mediaPlayer.setOnCompletionListener (listener)
    }

    override fun getReadyMedia(url: String){
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    override fun handlerPostDelayed(run: Runnable, time: Long){
        handler.postDelayed(run, time)
    }

    override fun handlerPost(run: Runnable){
        handler.post(run)
    }

    override fun handlerCallBack(run: Runnable){
        handler.removeCallbacks(run)
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun returnCurrentPosition(): Int{
        return mediaPlayer.currentPosition
    }

    override fun playRelease(){
        mediaPlayer.release()
    }
}