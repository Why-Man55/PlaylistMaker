package com.example.playlistmaker.player.domain.api

interface MediaPlayRepository {
    fun getReadyMedia()
    fun handlerPostDelayed(time: Long)
    fun handlerPost()
    fun handlerCallBack()
    fun startPlayer()
    fun pausePlayer()
    fun returnCurrentPosition(): Int
    fun playRelease()
}