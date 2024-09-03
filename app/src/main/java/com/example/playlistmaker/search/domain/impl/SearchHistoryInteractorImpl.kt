package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.data.SearchHistoryRepository
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {

    override fun getHistory(): List<Track> {
        return repository.getHistory()
    }

    override fun saveTrack(track: Track) {
        repository.saveTrack(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}