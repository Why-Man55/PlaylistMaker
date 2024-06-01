package com.example.playlistmaker.player.data.dto

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.player.domain.api.MediaPlayRepository

class MediaPlayRepImpl(private val url: String, private val run: Runnable): MediaPlayRepository {
    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())

    override fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener) {
        mediaPlayer.setOnPreparedListener(listener)
    }

    override fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        mediaPlayer.setOnCompletionListener (listener)
    }

    override fun getReadyMedia(){
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    override fun handlerPostDelayed(time: Long){
        handler.postDelayed(run, time)
    }

    override fun handlerPost(){
        handler.post(run)
    }

    override fun handlerCallBack(){
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