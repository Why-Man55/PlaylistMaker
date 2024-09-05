package com.example.playlistmaker.media.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.MediaInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsFragmentViewModel(private val interactor: MediaInteractor) : ViewModel() {

    private val playlistsLiveData = MutableLiveData<List<Playlist>>()
    fun returnPlaylists(): LiveData<List<Playlist>> = playlistsLiveData

    fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getPlaylists().collect { playlists ->
                bind(playlists)
            }
        }
    }

    private fun bind(playlists: List<Playlist>) {
        playlistsLiveData.postValue(playlists)
    }
}