package com.example.playlistmaker.search.presentation

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.search.data.dto.HandlerControllerRepimpl
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.models.Track

class SearchViewModel(private val tracksInteractor: TrackInteractor):ViewModel() {
    companion object{
        fun getViewModelFactory(sp: SharedPreferences, context: Context): ViewModelProvider.Factory = viewModelFactory  {
            initializer{
                SearchViewModel(Creator.provideTrackInteractor(sp, context))
                }
            }

        private const val CLICK_DELAY = 1000L
        private const val SEARCH_DELAY = 2000L
    }
    private val handlerControllerRepimpl = HandlerControllerRepimpl()

    private var livaDataSearchRes = MutableLiveData<SearchVMObjects>()

    private val consumer = object : TrackInteractor.TracksConsumer {
        override fun consume(foundTracks: List<Track>?, errorMessage: Int?) {
            livaDataSearchRes.postValue(SearchVMObjects(foundTracks, errorMessage, tracksInteractor.getHistory()))
        }

    }

    fun searchTrack(text: String){
        tracksInteractor.searchTrack(text, consumer)
    }
    fun loadHistory(){
        livaDataSearchRes.postValue(SearchVMObjects(listOf(), -2, tracksInteractor.getHistory()))
    }

    fun getSearchRes(): LiveData<SearchVMObjects> = livaDataSearchRes
    fun clearHistory(){
        tracksInteractor.clearHistory()
    }

    fun callBackHandler(runnable: Runnable){
        handlerControllerRepimpl.removeCallback(runnable)
    }

    fun delayClick(runnable: Runnable){
        handlerControllerRepimpl.postDelay(runnable, CLICK_DELAY)
    }
    fun delaySearch(runnable: Runnable){
        handlerControllerRepimpl.postDelay(runnable, SEARCH_DELAY)
    }
}