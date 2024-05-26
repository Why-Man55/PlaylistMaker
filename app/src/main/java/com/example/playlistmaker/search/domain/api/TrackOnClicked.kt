package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface TrackOnClicked{
    fun getTrackAndStart(track: Track)
}