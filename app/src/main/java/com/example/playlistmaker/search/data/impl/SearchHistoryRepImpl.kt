package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.HistoryControlRepository
import com.example.playlistmaker.search.data.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track

class SearchHistoryRepImpl(private val historyController: HistoryControlRepository) :
    SearchHistoryRepository {
    override fun getHistory(): List<Track> {
        return historyController.load()
    }

    override fun saveTrack(track: Track) {
        historyController.save(track)
    }

    override fun clearHistory() {
        historyController.clearHistory()
    }
}