package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import android.os.Handler
import com.example.playlistmaker.media.data.db.dao.TrackDao
import com.example.playlistmaker.player.data.MediaPlayRepository

class MediaPlayRepImpl(private val mediaPlayer: MediaPlayer): MediaPlayRepository {

    override fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener) {
        mediaPlayer.setOnPreparedListener(listener)
    }

    override fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        mediaPlayer.setOnCompletionListener (listener)
    }

    override fun getReadyMedia(url: String?){
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
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
        mediaPlayer.reset()
    }
}