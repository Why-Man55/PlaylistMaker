package com.example.playlistmaker.search.domain

interface HandlerControllerInt {
    fun removeCallback(runnable: Runnable)
    fun postDelay(runnable: Runnable, delay:Long)
}