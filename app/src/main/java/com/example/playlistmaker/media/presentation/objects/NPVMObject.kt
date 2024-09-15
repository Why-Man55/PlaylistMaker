package com.example.playlistmaker.media.presentation.objects

import com.example.playlistmaker.media.domain.model.Playlist

data class NPVMObject(
    val isAgain: Boolean,
    val playlist: Playlist
)