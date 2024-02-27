package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sP: SharedPreferences) {
    private val jSon = sP.getString(HISTORY_KEY, null)
    class Token : TypeToken<ArrayList<Track>>()
    private val list: ArrayList<Track> = if (jSon == null) ArrayList() else Gson().fromJson(jSon, Token().type)
    fun load(): List<Track>{
        return list.reversed()
    }

    fun save(trackForSave: Track){
        var isContains = false
        for(i in list){
            if (trackForSave.trackID == i.trackID){
                list.remove(i)
                list.add(0, i)
                isContains = true
            }
        }
        if(!isContains){
            list.add(trackForSave)
            if (list.count() > MAX_SIZE)
            {
                list.removeAt(0)
            }
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
        private const val MAX_SIZE = 10
    }
}