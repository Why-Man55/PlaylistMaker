package com.example.playlistmaker.search.data.dto

import android.content.SharedPreferences
import com.google.gson.Gson

class HistoryMemory(private val sP:SharedPreferences) {
    companion object{
        private const val HISTORY_KEY = "key_for_historySP"
    }

    fun editSP(text: String?){
        sP.edit()
            .putString(HISTORY_KEY, text)
            .apply()
    }
    fun returnNullJSon(): String?{
        return sP.getString(HISTORY_KEY, Gson().toJson(null))
    }
    
}