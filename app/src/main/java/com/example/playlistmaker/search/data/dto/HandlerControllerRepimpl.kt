package com.example.playlistmaker.search.data.dto

import android.os.Handler
import android.os.Looper

class HandlerControllerRepimpl{

    private val handler = Handler(Looper.getMainLooper())
    fun postDelay(runnable:Runnable, delay:Long) {
        handler.postDelayed(runnable, delay)
    }

    fun removeCallback(runnable:Runnable) {
        handler.removeCallbacks(runnable)
    }
}