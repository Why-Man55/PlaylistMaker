package com.example.playlistmaker.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.data.dto.MediaPlayRepImpl

class PlayerViewModel(val playerInter: MediaPlayRepImpl): ViewModel() {
    companion object{
        fun getViewModelFactory(url: String, run: Runnable): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(Creator.getMediaPlay(run, url)) as T
            }
        }
    }
    fun getReadyMedia(){
        playerInter.getReadyMedia()
    }

    fun handlerPostDelayed(time: Long){
        playerInter.handlerPostDelayed(time)
    }

    fun handlerPost(){
        playerInter.handlerPost()
    }

    fun handlerCallBack(){
        playerInter.handlerCallBack()
    }

    fun startPlayer(){
        playerInter.startPlayer()
    }

    fun pausePlayer(){
        playerInter.pausePlayer()
    }

    fun returnCurrentPosition(): Int{
        return playerInter.returnCurrentPosition()
    }

    fun playRelease(){
        playerInter.playRelease()
    }
}