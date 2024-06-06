package com.example.playlistmaker.search.data.dto

import com.example.playlistmaker.search.domain.models.Track

data class SearchResponseElements(val tracks:List<Track>, val error:Int)
