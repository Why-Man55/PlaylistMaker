package com.example.playlistmaker.search.presentation

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.data.dto.HandlerControllerRepimpl
import com.example.playlistmaker.search.data.dto.RetrofitControllerRepImpl
import com.example.playlistmaker.search.data.dto.SearchHistoryRepImpl
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.search.domain.api.RetrofitControllerRepository
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.TrackOnClicked
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.HistoryAdapter
import com.example.playlistmaker.search.ui.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create

class SearchViewModel(private val searchHistoryRep: SearchHistoryRepository, private val retrofitControllerRepImpl: RetrofitControllerRepository):ViewModel() {
    companion object{
        fun getViewModelFactory(sp:SharedPreferences): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(Creator.getSearchHistory(sp), Creator.getRetrofitController()) as T
                }
            }

        private const val ZERO_COUNT = 0
        private const val CLICK_DELAY = 1000L
    }
    private val handlerControllerRepimpl = HandlerControllerRepimpl()

    private var isSuccess = false
    private var isEmptyRes = false
    private var internetError = false
    private var body:TrackResponse? = TrackResponse(0, listOf())

    private var liveDataStates = MutableLiveData(listOf<Boolean>())
    private var liveDataAdapter = MutableLiveData<TrackAdapter>()
    private var liveDataHisAdapter = MutableLiveData<HistoryAdapter>()

    fun searchTrack(text: String){
        createRetrofit().create(ITunesApi::class.java)
            .search(text).enqueue(object : Callback<TrackResponse>{
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    isSuccess = response.isSuccessful
                    isEmptyRes = response.body()?.resultCount == ZERO_COUNT
                    body = response.body()
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    internetError = true
                }
            })
        liveDataStates.postValue(listOf(isSuccess, isEmptyRes, internetError))
    }

    fun getStatesSearch(): LiveData<List<Boolean>> = liveDataStates
    fun getTrackAdapter(trackOnClicked: TrackOnClicked): LiveData<TrackAdapter> {
        liveDataAdapter.postValue(TrackAdapter(body, this, trackOnClicked))
        return liveDataAdapter
    }
    fun getHisAdapter(trackOnClicked: TrackOnClicked): LiveData<HistoryAdapter> {
        liveDataHisAdapter.postValue(HistoryAdapter(body?.results,trackOnClicked))
        return liveDataHisAdapter
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

    fun callBackHandler(runnable: Runnable){
        handlerControllerRepimpl.removeCallback(runnable)
    }

    fun delayHandler(runnable: Runnable){
        handlerControllerRepimpl.postDelay(runnable, CLICK_DELAY)
    }
    private fun createRetrofit():Retrofit{
        return retrofitControllerRepImpl.createRetrofit()
    }
}