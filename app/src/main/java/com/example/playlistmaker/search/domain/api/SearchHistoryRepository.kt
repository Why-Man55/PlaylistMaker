package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryRepository {
    fun load(): List<Track>
    fun save(trackForSave: Track)
    fun clearHistory()
}