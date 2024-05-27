package com.example.playlistmaker.search.presentation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.impl.SearchHistoryReplmpl
import com.example.playlistmaker.search.domain.models.Track

class SearchViewModel(val searchHistoryRep: SearchHistoryReplmpl):ViewModel() {
    companion object{
        fun getViewModelFactory(sp:SharedPreferences): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(Creator.getSearchHistory(sp)) as T
                }
            }
    }

    fun load():List<Track>{
        return searchHistoryRep.load()
    }
    fun save(trackForSave: Track){
        searchHistoryRep.save(trackForSave)
    }
    fun clearHistory(){
        searchHistoryRep.clearHistory()
    }
}