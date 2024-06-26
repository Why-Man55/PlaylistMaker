package com.example.playlistmaker.player.presentation

import android.content.Intent
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

class PlayerViewModel(private val playerInter: PlayerInteractor): ViewModel() {


    private var playerState = STATE_DEFAULT
    private var playerLiveData = MutableLiveData<PlayerVMObjects>()

    private lateinit var gsonTrack: Track
    private lateinit var runnableTrack: Runnable
    fun getTrack(intent: Intent):LiveData<PlayerVMObjects> {
        returnTrack(intent)
        return playerLiveData
    }

    private fun returnTrack(intent: Intent){
        gsonTrack = Gson().fromJson(intent.extras?.getString("track"), Track::class.java)
        runnableTrack = runTime(gsonTrack)
        playerLiveData.value = PlayerVMObjects(0, gsonTrack)
    }

    fun getReadyMedia(){
        playerInter.getReadyMedia(gsonTrack.audioUrl)
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
        playerInter.handlerPostDelayed(runnableTrack,SET_TIME_WAIT)
    }

    private fun runStatus():Boolean{
        return playerState == STATE_PLAYING
    }

    private fun handlerPost(){
        playerInter.handlerPost(runnableTrack)
    }

    fun handlerCallBack(){
        playerInter.handlerCallBack(runnableTrack)
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

    private fun runTime(track: Track): Runnable{
        return Runnable {
            if(runStatus()){
                handlerPostDelayed()
                playerLiveData.value = PlayerVMObjects(returnCurrentPosition().toLong(), track)
            }
        }
    }

    companion object{
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val SET_TIME_WAIT = 400L
    }
}