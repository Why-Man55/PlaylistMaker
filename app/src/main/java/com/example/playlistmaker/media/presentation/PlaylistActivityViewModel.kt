package com.example.playlistmaker.media.presentation

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

class PlaylistActivityViewModel(private val interactor: MediaInteractor):ViewModel() {

    private val playlistLiveData = MutableLiveData<Playlist>()

    fun returnPlaylist(intent: Intent):LiveData<Playlist>{
        getPlaylist(Gson().fromJson(intent.extras?.getString("playlist"), Playlist::class.java).id)
        return playlistLiveData
    }

    private fun getPlaylist(id:Long){
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getPlaylist(id).collect{
                bind(it)
            }
        }
    }

    private fun bind(playlist: Playlist){
        playlistLiveData.postValue(playlist)
    }
}