package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitController(private val iTunesService:ITunesApi,private val context: Context): NetworkClient {

    override suspend fun doSearch(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { searchState = -1 }
        }
        if (dto !is TrackSearchRequest) {
            return Response().apply { searchState = 400 }
        }
        return withContext(Dispatchers.IO){
            try {
                val resp = iTunesService.search(dto.expression)
                resp.apply { searchState = 200 }
            } catch (e:Throwable){
                Response().apply { searchState = 500 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}