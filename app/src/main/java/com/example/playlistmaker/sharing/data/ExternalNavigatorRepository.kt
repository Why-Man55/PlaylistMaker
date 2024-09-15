package com.example.playlistmaker.sharing.data

import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigatorRepository {
    fun shareLink(text: String)
    fun openLink(text: String)
    fun openEmail(email: EmailData)
    fun sharePlaylist(playlist: Playlist, tracks: List<Track>)
}