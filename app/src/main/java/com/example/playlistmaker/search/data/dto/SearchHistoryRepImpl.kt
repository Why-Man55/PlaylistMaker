package com.example.playlistmaker.search.data.dto

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepImpl(sP: SharedPreferences): SearchHistoryRepository {

    private val historyMemoryRepImpl = HistoryMemoryRepImpl(sP)

    private val jSon = historyMemoryRepImpl.returnNullJSon()
    class Token : TypeToken<ArrayList<Track>>()
    private val list: ArrayList<Track> = if (jSon == Gson().toJson(null)) ArrayList() else Gson().fromJson(jSon, Token().type)
    override fun load(): List<Track>{
        return list.reversed()
    }

    override fun save(trackForSave: Track){
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
            historyMemoryRepImpl.editSP(null)
        }
        else{
            historyMemoryRepImpl.editSP(addJSon)
        }
    }

    override fun clearHistory(){
        list.clear()
        val emptyJSon = null
        historyMemoryRepImpl.editSP(emptyJSon)
    }

    companion object{
        private const val MAX_SIZE = 10
    }
}