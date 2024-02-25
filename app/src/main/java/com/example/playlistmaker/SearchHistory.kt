package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sP: SharedPreferences) {
    private val jSon = sP.getString(HISTORY_KEY, "")
    class Token : TypeToken<ArrayList<Track>>()
    private val list: ArrayList<Track> = Gson().fromJson(Gson().toJson(jSon), Token().type)
    fun load(): List<Track>{
        return list.reversed()
    }

    fun save(trackForSave: Track){
        list.add(trackForSave)
        if (list.count() > 10)
        {
            list.removeAt(0)
        }
        val addJSon = Gson().toJson(list)
        sP.edit()
            .putString(HISTORY_KEY, addJSon)
            .apply()
    }

    fun clearHistory(){
        list.clear()
        val emptyJSon = Gson().toJson(list)
        sP.edit()
            .putString(HISTORY_KEY, emptyJSon)
            .apply()
    }

    companion object{
        private const val HISTORY_KEY = "key_for_historySP"
    }
}