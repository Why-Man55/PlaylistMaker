package com.example.playlistmaker.player.domain.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.MediaPlayRepository
import com.example.playlistmaker.player.domain.PlayerInteractor

class PlayerInteractorImpl(private val repository: MediaPlayRepository): PlayerInteractor {
    override fun getReadyMedia(url:String?) {
        repository.getReadyMedia(url)
    }

    override fun handlerPost(run:Runnable) {
        repository.handlerPost(run)
    }

    override fun handlerCallBack(run:Runnable) {
        repository.handlerCallBack(run)
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun playRelease() {
        repository.playRelease()
    }

    override fun returnCurrentPosition(): Int {
        return repository.returnCurrentPosition()
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun handlerPostDelayed(run:Runnable,time: Long) {
        repository.handlerPostDelayed(run,time)
    }

    override fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        repository.setOnCompletionListener(listener)
    }

    override fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener) {
        repository.setOnPreparedListener(listener)
    }
}
