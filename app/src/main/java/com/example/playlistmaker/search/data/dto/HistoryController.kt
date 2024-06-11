package com.example.playlistmaker.search.data.dto

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryController(private val sp: SharedPreferences) {

    private val jSon = sp.getString(HISTORY_KEY, Gson().toJson(null))
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
            editSP(null)
        }
        else{
            editSP(addJSon)
        }
    }

    fun clearHistory(){
        list.clear()
        editSP(null)
    }

    private fun editSP(text:String?){
        sp.edit()
            .putString(HISTORY_KEY,text)
            .apply()
    }

    companion object{
        private const val MAX_SIZE = 10
        private const val HISTORY_KEY = "key_for_historySP"
    }
}