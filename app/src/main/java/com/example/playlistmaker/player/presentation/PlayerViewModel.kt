package com.example.playlistmaker.player.presentation

import android.content.Intent
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.MediaInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(private val playerInter: PlayerInteractor, private val db: MediaInteractor) :
    ViewModel() {


    private var playerState = STATE_DEFAULT
    private var playerLiveData = MutableLiveData<PlayerVMObjects>()

    private var timerJob: Job? = null

    private var playlistList: List<Playlist> = listOf()
    private var linked = false
    private var isEnded = false

    private lateinit var gsonTrack: Track
    fun getTrack(intent: Intent): LiveData<PlayerVMObjects> {
        returnTrack(intent)
        return playerLiveData
    }

    private fun returnTrack(intent: Intent) {
        gsonTrack = Gson().fromJson(intent.extras?.getString("track"), Track::class.java)
        checkLiked(gsonTrack.trackID)
        playerLiveData.value = PlayerVMObjects(0, gsonTrack, playlistList, linked)
    }

    private fun returnCurrentPosition(): Int {
        return playerInter.returnCurrentPosition()
    }

    private fun runTime() {
        timerJob = viewModelScope.launch {
            while (runStatus()) {
                delay(SET_TIME_WAIT)
                bind()
            }
        }
    }

    private fun runStatus(): Boolean {
        return playerState == STATE_PLAYING
    }

    private fun startPlayer() {
        playerInter.startPlayer()
    }

    private fun checkLiked(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            linked = id in db.getFavID()
        }
    }

    fun getReadyMedia() {
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

    fun isPlaying(): Boolean {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
                playerState = STATE_PAUSED
                return true
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                playerState = STATE_PLAYING
                runTime()
                isEnded = false
                return false
            }

            else -> {
                return false
            }
        }
    }

    fun stopTimer() {
        isEnded = true
        timerJob?.cancel()
    }

    fun pausePlayer() {
        playerInter.pausePlayer()
        playerState = STATE_PAUSED
    }

    fun playRelease() {
        playerInter.playRelease()
    }

    fun isFavClicked(isFav: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isFav) {
                db.deleteTrack(gsonTrack)
            } else {
                db.changeFavorites(gsonTrack)
            }
            checkLiked(gsonTrack.trackID)
        }
    }

    fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            db.getPlaylists().collect { playlists ->
                playlistList = playlists
                bind()
            }
        }
    }

    fun updatePlaylists(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) { db.updatePlaylist(playlist) }
    }

    fun bind() {
        playerLiveData.postValue(
            if(!isEnded){
                PlayerVMObjects(
                    returnCurrentPosition().toLong(),
                    gsonTrack,
                    playlistList,
                    linked
                )
            }
            else{
                PlayerVMObjects(
                    0L,
                    gsonTrack,
                    playlistList,
                    linked
                )
            }
        )
    }

    fun insertPlaylistTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            db.insertPlaylistTrack(track)
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val SET_TIME_WAIT = 300L
    }
}