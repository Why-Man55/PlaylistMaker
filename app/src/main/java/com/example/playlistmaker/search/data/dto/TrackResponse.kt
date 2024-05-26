package com.example.playlistmaker.search.data.dto

import com.example.playlistmaker.search.domain.models.Track

class TrackResponse (
    val resultCount: Int,
    val results: List<Track>
)
