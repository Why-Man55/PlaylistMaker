package com.example.playlistmaker.search.data.dto

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackDto

class TrackResponse (
    val resultCount: Int,
    val results: List<TrackDto>
): Response()
