package com.example.playlistmaker.media.domain.model

import com.example.playlistmaker.search.domain.models.Track

interface PlaylistLongClicked {
    fun deleteTrack(track: Track)
}