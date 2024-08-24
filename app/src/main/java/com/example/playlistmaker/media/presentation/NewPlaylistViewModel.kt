package com.example.playlistmaker.media.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.MediaInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val interactor:MediaInteractor):ViewModel() {
    fun updatePlaylists(playlist: Playlist){
        viewModelScope.launch(Dispatchers.IO) {
            interactor.insertPlaylists(playlist)
        }
    }
}