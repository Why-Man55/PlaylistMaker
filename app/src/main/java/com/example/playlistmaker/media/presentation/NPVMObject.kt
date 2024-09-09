package com.example.playlistmaker.media.presentation

import com.example.playlistmaker.media.domain.model.Playlist

data class NPVMObject(
    val uri: String?,
    val playlist: Playlist?
)