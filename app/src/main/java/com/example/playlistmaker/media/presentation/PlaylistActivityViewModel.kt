package com.example.playlistmaker.media.presentation

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.MediaInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.presentation.objects.PAVMObject
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistActivityViewModel(private val interactor: MediaInteractor, private val share:SharingInteractor):ViewModel() {

    private val playlistLiveData = MutableLiveData<PAVMObject>()

    private lateinit var thisPlaylist: Playlist
    private var thisTracks:List<Track> = listOf()
    private var trackTime = 0L

    fun returnPlaylist(intent: Intent):LiveData<PAVMObject>{
        getPlaylist(intent)
        return playlistLiveData
    }

    fun sharePlaylist(){
        share.sharePlaylist(thisPlaylist,thisTracks)
    }

    fun deletePlaylist(){
        viewModelScope.launch(Dispatchers.IO) {
            interactor.deletePlaylist(thisPlaylist)
        }
    }

    fun updatePlaylist(id:String){
        viewModelScope.launch {
            interactor.updatePlaylist(Playlist(thisPlaylist.name,
                thisPlaylist.image,
                thisPlaylist.count - 1,
                thisPlaylist.info,
                thisPlaylist.content.replace("$id, ", ""),
                thisPlaylist.id))
        }
    }

    fun bindAgain(){
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getPlaylist(thisPlaylist.id).collect{
                thisPlaylist = it
                getPlaylistTracks(it.content)
                bind()
            }
        }
    }

    private fun getPlaylist(intent: Intent){
        thisPlaylist = Gson().fromJson(intent.extras?.getString("playlist"), Playlist::class.java)
        getPlaylistTracks(thisPlaylist.content)
    }

    private fun getPlaylistTracks(id:String){
        val mutableList = mutableListOf<Track>()
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getPlaylistTracks().collect{
                it.map{ track ->
                    if(track.trackID.toString() in id){
                        mutableList.add(track)
                    }
                }
                thisTracks = mutableList
                tracksTime(thisTracks)
            }
        }
    }

    private fun tracksTime(tracks:List<Track>){
        var time = 0L
        tracks.map{
            time += it.trackTimeItem
        }
        trackTime = time
        bind()
    }

    private fun bind(){
        playlistLiveData.postValue(PAVMObject(thisPlaylist, thisTracks, trackTime))
    }
}