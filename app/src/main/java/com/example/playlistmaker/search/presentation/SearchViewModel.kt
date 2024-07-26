package com.example.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor: TrackInteractor, private val searchHistoryInt: SearchHistoryInteractor):ViewModel() {
    companion object{
        private const val SEARCH_DELAY = 2000L
    }

    private var lastRequest: String? = null
    private var searchJob: Job? = null
    private var liveDataSearchRes = MutableLiveData<SearchVMObjects>()

    fun searchTrack(text: String){
        if (text == lastRequest){
            return
        }

        lastRequest = text

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY)
            searchRequest(text)
        }
    }

    private fun searchRequest(newSearchText: String){
        if(newSearchText.isNotEmpty()){
            viewModelScope.launch {
                tracksInteractor
                    .searchTrack(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }


    private fun processResult(foundNames: List<Track>?, errorMessage: Int?) {
        val persons = mutableListOf<Track>()
        if (foundNames != null) {
            persons.addAll(foundNames)
        }

        when {
            errorMessage == null -> {
                renderSearch(SearchVMObjects(persons, 200, listOf()))
            }
            else -> {
                renderSearch(SearchVMObjects(listOf(), -1, listOf()))
            }
        }
    }

    fun loadHistory(){
        renderSearch(SearchVMObjects(listOf(), -2, searchHistoryInt.getHistory()))
    }

    fun saveTrack(track: Track){
        searchHistoryInt.saveTrack(track)
    }

    fun getSearchRes(): LiveData<SearchVMObjects> = liveDataSearchRes
    fun clearHistory(){
        searchHistoryInt.clearHistory()
    }

    private fun renderSearch(searchObject: SearchVMObjects){
        liveDataSearchRes.postValue(searchObject)
    }
}