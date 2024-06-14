package com.example.playlistmaker.search.data.impl

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.search.data.HandlerControllerRep

class HandlerControllerRepimpl: HandlerControllerRep {

    private val handler = Handler(Looper.getMainLooper())
    override fun postDelay(runnable:Runnable, delay:Long) {
        handler.postDelayed(runnable, delay)
    }

    override fun removeCallback(runnable:Runnable) {
        handler.removeCallbacks(runnable)
    }
}