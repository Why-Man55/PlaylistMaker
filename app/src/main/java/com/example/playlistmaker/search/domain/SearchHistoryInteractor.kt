package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {
    fun getHistory(): List<Track>
    fun saveTrack(track: Track)
    fun clearHistory()
}