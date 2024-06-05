package com.example.playlistmaker.search.data.dto

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.search.domain.api.HandlerControllerRepository

class HandlerControllerRepimpl: HandlerControllerRepository {

    private val handler = Handler(Looper.getMainLooper())
    override fun postDelay(runnable:Runnable, delay: Long) {
        handler.postDelayed(runnable, delay)
    }

    override fun removeCallback(runnable:Runnable) {
        handler.removeCallbacks(runnable)
    }
}