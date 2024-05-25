package com.example.playlistmaker.doamin.api

import com.example.playlistmaker.doamin.models.Track

interface TrackOnClicked{
    fun getTrackAndStart(track: Track)
}