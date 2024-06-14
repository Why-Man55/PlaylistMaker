package com.example.playlistmaker.search.data

interface HandlerControllerRep {
    fun postDelay(runnable:Runnable, delay:Long)
    fun removeCallback(runnable:Runnable)
}