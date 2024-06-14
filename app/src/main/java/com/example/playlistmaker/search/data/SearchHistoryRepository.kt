package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryRepository {
    fun getHistory(): List<Track>
    fun saveTrack(track: Track)
    fun clearHistory()
}