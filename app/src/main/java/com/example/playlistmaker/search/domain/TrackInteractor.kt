package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: TracksConsumer)

    fun saveTrack(track:Track)
    fun getHistory():List<Track>
    fun clearHistory()

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: Int?)
    }
}