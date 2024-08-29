package com.example.playlistmaker.media.presentation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.MediaInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.Date

class NewPlaylistViewModel(private val interactor:MediaInteractor):ViewModel() {
    private var fileLiveData = MutableLiveData<Uri>()

    fun getFile():LiveData<Uri>{
        return fileLiveData
    }

    fun updatePlaylists(playlist: Playlist){
        viewModelScope.launch(Dispatchers.IO) {
            interactor.insertPlaylists(playlist)
        }
    }

    fun saveImage(context: Context, name: String, inputStream: InputStream?, time: Date):Uri{
        return interactor.saveImage(context,name,inputStream,time)
    }

    fun loadImage(context: Context, time:Date, name: String){
        viewModelScope.launch(Dispatchers.IO) {
            fileLiveData.postValue(interactor.loadImage(context,time,name))
        }
    }
}