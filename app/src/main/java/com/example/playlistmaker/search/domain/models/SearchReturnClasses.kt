package com.example.playlistmaker.search.domain.models

import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository

data class SearchReturnClasses(
    val responseStates: ResponseStates,
    val response:TrackResponse?
)
