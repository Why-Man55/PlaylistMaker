package com.example.playlistmaker.search.presentation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.data.dto.RetrofitController
import com.example.playlistmaker.search.domain.impl.SearchHistoryReplmpl
import com.example.playlistmaker.search.domain.models.Track
import retrofit2.Retrofit

class SearchViewModel(private val searchHistoryRep: SearchHistoryReplmpl, private val retrofitController: RetrofitController):ViewModel() {
    companion object{
        fun getViewModelFactory(sp:SharedPreferences): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(Creator.getSearchHistory(sp), Creator.getRetrofitController()) as T
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
    fun createRetrofit(url:String):Retrofit{
        return retrofitController.createRetrofit(url)
    }
}