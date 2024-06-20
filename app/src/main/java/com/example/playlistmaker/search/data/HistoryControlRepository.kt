package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.models.Track

interface HistoryControlRepository {
    fun load(): List<Track>
    fun save(trackForSave: Track)
    fun clearHistory()
}