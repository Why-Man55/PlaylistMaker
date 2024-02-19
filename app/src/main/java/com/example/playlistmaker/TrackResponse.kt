package com.example.playlistmaker

import com.google.gson.annotations.SerializedName

class TrackResponse (
    val resultCount: Int,
    val results: List<Track>
)
