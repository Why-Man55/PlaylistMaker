package com.example.playlistmaker.search.presentation

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.data.dto.HandlerControllerRepimpl
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.models.Track

class SearchViewModel(private val searchHistoryRep: SearchHistoryRepository, private val tracksInteractor: TrackInteractor):ViewModel() {
    companion object{
        fun getViewModelFactory(sp: SharedPreferences): ViewModelProvider.Factory = viewModelFactory  {
            initializer{
                SearchViewModel(Creator.getSearchHistory(sp), Creator. provideTrackInteractor())
                }
            }

        private const val CLICK_DELAY = 1000L
        private const val SEARCH_DELAY = 2000L
    }
    private val handlerControllerRepimpl = HandlerControllerRepimpl()

    private var liveDataResponseStates = MutableLiveData<List<Track>?>()
    private var liveDataCode = MutableLiveData<Int>()
    private var liveDataLoadHis = MutableLiveData<List<Track>>()
    private var liveDataSHRep = MutableLiveData<SearchHistoryRepository>()

    init{
        liveDataSHRep.value = searchHistoryRep
    }

    private val consumer = object : TrackInteractor.TracksConsumer {
        override fun consume(foundTracks: List<Track>?, code:Int) {
            liveDataCode.postValue(code)
            liveDataResponseStates.postValue(foundTracks)
        }

    }

    fun searchTrack(text: String){
        tracksInteractor.searchTrack(text, consumer)
    }

    fun getStatesSearch(): LiveData<List<Track>?> = liveDataResponseStates
    fun getCodeType(): LiveData<Int> = liveDataCode
    fun getHistory():LiveData<List<Track>> = liveDataLoadHis
    fun getSearchRep():LiveData<SearchHistoryRepository> = liveDataSHRep
    fun clearHistory(){
        searchHistoryRep.clearHistory()
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