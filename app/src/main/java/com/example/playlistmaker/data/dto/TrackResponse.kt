package com.example.playlistmaker.data.dto

import com.example.playlistmaker.doamin.models.Track

class TrackResponse (
    val resultCount: Int,
    val results: List<Track>
)
