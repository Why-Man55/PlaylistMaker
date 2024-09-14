package com.example.playlistmaker.media.presentation

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.MediaInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.presentation.objects.NPVMObject
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.Date

class NewPlaylistViewModel(private val interactor: MediaInteractor) : ViewModel() {
    private var fileLiveData = MutableLiveData<NPVMObject>()
    private var thisPlaylist:Playlist? = null

    fun getFile(intent: Intent): LiveData<NPVMObject> {
        getPlaylist(intent)

        return fileLiveData
    }

    private fun getPlaylist(intent: Intent){
        val playlistText = intent.extras?.getString("playlist")!!
        if (playlistText.isEmpty()) {
            thisPlaylist = null
            fileLiveData.postValue(NPVMObject("", null))
        } else {
            val playlist = Gson().fromJson(playlistText, Playlist::class.java)
            thisPlaylist = playlist
            fileLiveData.postValue(NPVMObject(thisPlaylist!!.image, thisPlaylist))
        }
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
        fileLiveData.postValue(
            NPVMObject(
                interactor.saveImage(context, name, inputStream, time).toString(),
                thisPlaylist
            )
        )
    }
}