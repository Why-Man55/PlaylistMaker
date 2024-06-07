package com.example.playlistmaker.player.presentation

import android.content.Intent
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

class PlayerViewModel: ViewModel() {

    private lateinit var playerInter: PlayerInteractor

    private var playerState = STATE_DEFAULT
    private var liveDataTime = MutableLiveData<Long>()
    private var liveDataTrack = MutableLiveData<Track>()
    fun getPlayerStates(): LiveData<Long> = liveDataTime
    fun getTrack(intent: Intent):LiveData<Track> {
        returnTrack(intent)
        return liveDataTrack
    }

    private fun returnTrack(intent: Intent){
        val gSon = Gson().fromJson(intent.extras?.getString("track"), Track::class.java)
        liveDataTrack.value = gSon
        playerInter = Creator.providePlayerInteractor(gSon.audioUrl, runTime())
    }

    fun getReadyMedia(){
        playerInter.getReadyMedia()
    }

    fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener) {
        playerInter.setOnPreparedListener(listener)
        playerState = STATE_PREPARED
    }

    fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        playerInter.setOnCompletionListener(listener)
        playerState = STATE_PREPARED
    }

    private fun handlerPostDelayed(){
        playerInter.handlerPostDelayed(SET_TIME_WAIT)
    }

    private fun runStatus():Boolean{
        return playerState == STATE_PLAYING
    }

    private fun handlerPost(){
        playerInter.handlerPost()
    }

    fun handlerCallBack(){
        playerInter.handlerCallBack()
    }

    private fun startPlayer(){
        playerInter.startPlayer()
    }

    fun isPlaying():Boolean{
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
                playerState = STATE_PAUSED
                return true
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                playerState = STATE_PLAYING
                handlerPost()
                return false
            }
            else ->{
                return false
            }
        }
    }

    fun pausePlayer(){
        playerInter.pausePlayer()
        playerState = STATE_PAUSED
    }

    private fun returnCurrentPosition(): Int{
        return playerInter.returnCurrentPosition()
    }

    fun playRelease(){
        playerInter.playRelease()
    }

    private fun runTime(): Runnable{
        return Runnable {
            if(runStatus()){
                handlerPostDelayed()
                liveDataTime.value = returnCurrentPosition().toLong()
            }
        }
    }

    companion object{
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel() as T
                }
            }
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val SET_TIME_WAIT = 400L
    }
}