package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.search.data.HistoryControlRepository
import com.example.playlistmaker.search.data.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track

class SearchHistoryRepImpl(private val historyController:HistoryControlRepository, private val dao: AppDatabase):SearchHistoryRepository {
    override fun getHistory(): List<Track> {
        val idList = dao.trackDao().getTrackID()
        val returnList = historyController.load()
        returnList.map { track ->
            if(track.trackID in idList){
                track.isFavorite = true
            }
        }
        return returnList
    }

    override fun saveTrack(track: Track) {
        historyController.save(track)
    }

    override fun clearHistory() {
        historyController.clearHistory()
    }
}