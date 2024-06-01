package com.example.playlistmaker.search.data.dto

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.api.HistoryMemoryRepository
import com.google.gson.Gson

class HistoryMemoryRepImpl(private val sP:SharedPreferences): HistoryMemoryRepository {
    companion object{
        private const val HISTORY_KEY = "key_for_historySP"
    }

    override fun editSP(text: String?){
        sP.edit()
            .putString(HISTORY_KEY, text)
            .apply()
    }
    override fun returnNullJSon(): String?{
        return sP.getString(HISTORY_KEY, Gson().toJson(null))
    }
    
}