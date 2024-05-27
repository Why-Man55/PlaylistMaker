package com.example.playlistmaker.search.domain.impl

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.dto.HistoryMemory
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryReplmpl(sP: SharedPreferences): SearchHistoryRepository {

    private val historyMemory = HistoryMemory(sP)

    private val jSon = historyMemory.returnNullJSon()
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
            historyMemory.editSP(null)
        }
        else{
            historyMemory.editSP(addJSon)
        }
    }

    override fun clearHistory(){
        list.clear()
        val emptyJSon = null
        historyMemory.editSP(emptyJSon)
    }

    companion object{
        private const val MAX_SIZE = 10
    }
}