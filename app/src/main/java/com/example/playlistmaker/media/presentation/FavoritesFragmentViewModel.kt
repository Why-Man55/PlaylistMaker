package com.example.playlistmaker.media.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.MediaInteractor
import com.example.playlistmaker.media.presentation.objects.FFVMObject
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(private val interactor: MediaInteractor) : ViewModel() {
    private val favoriteLiveData = MutableLiveData<FFVMObject>()
    fun getFavorites(): LiveData<FFVMObject> = favoriteLiveData

    fun getTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getTracks().collect { tracks ->
                processTracks(tracks)
            }
        }
    }

    private fun processTracks(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            bind(emptyList(), 1)
        } else {
            bind(tracks, null)
        }
    }

    private fun bind(tracks: List<Track>, error: Int?) {
        favoriteLiveData.postValue(FFVMObject(tracks, error))
    }
}