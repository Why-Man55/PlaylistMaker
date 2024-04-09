package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sP: SharedPreferences) {
    private val jSon = sP.getString(HISTORY_KEY, Gson().toJson(null))
    class Token : TypeToken<ArrayList<Track>>()
    private val list: ArrayList<Track> = if (jSon == Gson().toJson(null)) ArrayList() else Gson().fromJson(jSon, Token().type)
    fun load(): List<Track>{
        return list.reversed()
    }

    fun save(trackForSave: Track){
        val isContains = list.any { trackForSave.trackID == it.trackID }
        if(isContains){
            list.removeIf { trackForSave.trackID == it.trackID }
            list.add(trackForSave)
        }
        else{
            list.add(trackForSave)
        }
        if (list.count() > MAX_SIZE)
        {
            list.removeAt(0)
        }
        val addJSon = Gson().toJson(list)
        if(list.isEmpty()){
            sP.edit()
                .putString(HISTORY_KEY, null)
                .apply()
        }
        else{
            sP.edit()
                .putString(HISTORY_KEY, addJSon)
                .apply()
        }
    }

    fun clearHistory(){
        list.clear()
        val emptyJSon = null
        sP.edit()
            .putString(HISTORY_KEY, emptyJSon)
            .apply()
    }

    companion object{
        private const val HISTORY_KEY = "key_for_historySP"
        private const val MAX_SIZE = 10
    }
}