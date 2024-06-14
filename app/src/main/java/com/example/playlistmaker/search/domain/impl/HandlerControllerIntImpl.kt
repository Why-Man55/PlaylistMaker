package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.data.HandlerControllerRep
import com.example.playlistmaker.search.domain.HandlerControllerInt

class HandlerControllerIntImpl(private val repository:HandlerControllerRep): HandlerControllerInt {
    override fun removeCallback(runnable: Runnable) {
        repository.removeCallback(runnable)
    }

    override fun postDelay(runnable: Runnable, delay: Long) {
        repository.postDelay(runnable,delay)
    }
}