package com.example.playlistmaker.media.presentation

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.MediaInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.Date

class NewPlaylistViewModel(private val interactor: MediaInteractor) : ViewModel() {
    private var fileLiveData = MutableLiveData<Playlist>()
    private lateinit var thisPlaylist:Playlist
    private var isFirstTime = true

    fun getFile(intent: Intent): LiveData<Playlist> {
        if(isFirstTime){
            getPlaylist(intent)
        }
        return fileLiveData
    }

    private fun getPlaylist(intent: Intent){
        val playlistText = intent.extras?.getString("playlist")!!
        if (playlistText.isEmpty()) {
            thisPlaylist = Playlist("", "", 0, "", "", 0L)

        } else {
            val playlist = Gson().fromJson(playlistText, Playlist::class.java)
            thisPlaylist = playlist
        }
        fileLiveData.postValue(thisPlaylist)
    }

    fun updatePlaylists(playlist: Playlist){
        viewModelScope.launch {
            interactor.updatePlaylist(playlist)
        }
    }

    fun insertPlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.insertPlaylists(playlist)
        }
    }

    fun saveImage(context: Context, name: String, inputStream: InputStream?, time: Date) {
        isFirstTime = false
        fileLiveData.postValue(
            Playlist(thisPlaylist.name,
                interactor.saveImage(context, name, inputStream, time).toString(),
                thisPlaylist.count,
                thisPlaylist.info,
                thisPlaylist.content,
                thisPlaylist.id)
        )
    }
}