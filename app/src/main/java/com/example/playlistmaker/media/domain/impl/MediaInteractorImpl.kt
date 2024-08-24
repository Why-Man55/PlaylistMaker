package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.data.MediaRepository
import com.example.playlistmaker.media.domain.MediaInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class MediaInteractorImpl(private val repository: MediaRepository): MediaInteractor {
    override fun getFavID(): List<Int> {
        return repository.getFavID()
    }
    override fun getTracks(): Flow<List<Track>> {
        return repository.getFavorites()
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun changeFavorites(track: Track) {
        repository.changeFavorites(track)
    }

    override suspend fun deleteTrack(track: Track) {
        repository.deleteTrack(track)
    }

    override suspend fun insertPlaylists(playlist: Playlist) {
        repository.insertPlaylists(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }
}