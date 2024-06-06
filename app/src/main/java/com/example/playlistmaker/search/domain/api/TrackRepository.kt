package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.data.dto.SearchResponseElements

interface TrackRepository {
    fun searchTracks(expression: String):SearchResponseElements
}