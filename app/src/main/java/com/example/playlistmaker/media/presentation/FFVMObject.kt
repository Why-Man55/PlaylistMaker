package com.example.playlistmaker.media.presentation

import com.example.playlistmaker.search.domain.models.Track

data class FFVMObject(
    val tracks: List<Track>,
    val error: Int?
)