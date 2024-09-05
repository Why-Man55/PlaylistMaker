package com.example.playlistmaker.media.domain.api

import com.example.playlistmaker.media.domain.model.Playlist

interface PlaylistOpen {
    fun startPlaylist(playlist: Playlist)
}