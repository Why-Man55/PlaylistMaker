package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest

class RetrofitController(private val iTunesService:ITunesApi,private val context: Context): NetworkClient {

    override fun doSearch(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { searchState = -1 }
        }
        if (dto !is TrackSearchRequest) {
            return Response().apply { searchState = 400 }
        }
        val resp = iTunesService.search(dto.expression).execute()

        val body = resp.body()
        return body?.apply { searchState = resp.code() } ?: Response().apply { searchState = resp.code() }
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