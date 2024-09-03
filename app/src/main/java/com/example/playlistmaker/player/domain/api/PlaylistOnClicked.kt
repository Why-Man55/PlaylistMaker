package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.media.domain.model.Playlist

interface PlaylistOnClicked {
    fun saveIntoPlaylist(playlist: Playlist)
}