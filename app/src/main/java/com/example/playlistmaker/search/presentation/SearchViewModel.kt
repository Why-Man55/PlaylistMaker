package com.example.playlistmaker.search.presentation

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.data.dto.HandlerControllerRepimpl
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.search.domain.api.RetrofitControllerRepository
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.ResponseStates
import com.example.playlistmaker.search.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

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

    private var liveDataResponseStates = MutableLiveData<ResponseStates>()
    private var liveDataLoadHis = MutableLiveData<List<Track>>()
    private var liveDataResponse = MutableLiveData<TrackResponse?>()

    fun searchTrack(text: String){
        var isSuccess = true
        var zeroCount = false
        var internetError = false
        createRetrofit().create(ITunesApi::class.java)
            .search(text).enqueue(object : Callback<TrackResponse>{
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    isSuccess = response.isSuccessful
                    zeroCount = response.body()?.resultCount == ZERO_COUNT
                    liveDataResponse.postValue(response.body())
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    internetError = true

                }
            })
        liveDataResponseStates.value = ResponseStates(isSuccess, zeroCount, internetError)
    }

    fun getStatesSearch(): LiveData<ResponseStates> = liveDataResponseStates
    fun getHistory():LiveData<List<Track>> = liveDataLoadHis
    fun getResponse():LiveData<TrackResponse?> = liveDataResponse

    fun load(){
        liveDataLoadHis.value = searchHistoryRep.load()
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