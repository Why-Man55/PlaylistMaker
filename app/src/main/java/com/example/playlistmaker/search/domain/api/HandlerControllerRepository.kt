package com.example.playlistmaker.search.domain.api

interface HandlerControllerRepository {
    fun postDelay(runnable:Runnable, delay:Long)
    fun removeCallback(runnable:Runnable)
}