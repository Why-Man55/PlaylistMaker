package com.example.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.HandlerControllerInt
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.models.Track

class SearchViewModel(private val tracksInteractor: TrackInteractor, private val searchHistoryInt: SearchHistoryInteractor,
    private val handler: HandlerControllerInt):ViewModel() {
    companion object{
        private const val CLICK_DELAY = 1000L
        private const val SEARCH_DELAY = 2000L
    }

    private var livaDataSearchRes = MutableLiveData<SearchVMObjects>()

    private val consumer = object : TrackInteractor.TracksConsumer {
        override fun consume(foundTracks: List<Track>?, errorMessage: Int?) {
            livaDataSearchRes.postValue(SearchVMObjects(foundTracks, errorMessage, searchHistoryInt.getHistory()))
        }

    }

    fun searchTrack(text: String){
        tracksInteractor.searchTrack(text, consumer)
    }
    fun loadHistory(){
        livaDataSearchRes.postValue(SearchVMObjects(listOf(), -2, searchHistoryInt.getHistory()))
    }

    fun saveTrack(track: Track){
        searchHistoryInt.saveTrack(track)
    }

    fun getSearchRes(): LiveData<SearchVMObjects> = livaDataSearchRes
    fun clearHistory(){
        searchHistoryInt.clearHistory()
    }

    fun callBackHandler(runnable: Runnable){
        handler.removeCallback(runnable)
    }

    fun delayClick(runnable: Runnable){
        handler.postDelay(runnable, CLICK_DELAY)
    }
    fun delaySearch(runnable: Runnable){
        handler.postDelay(runnable, SEARCH_DELAY)
    }
}