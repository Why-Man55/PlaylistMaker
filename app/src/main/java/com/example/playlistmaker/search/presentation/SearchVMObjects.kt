package com.example.playlistmaker.search.presentation

import com.example.playlistmaker.search.domain.models.Track

data class SearchVMObjects(val tracks: List<Track>?, val code: Int?, val history: List<Track>)