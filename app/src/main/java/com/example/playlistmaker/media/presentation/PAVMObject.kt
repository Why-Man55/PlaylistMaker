package com.example.playlistmaker.media.presentation

import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track

data class PAVMObject(val playlist: Playlist,
    val tracks:List<Track>,
    val tracksTime:Long)
