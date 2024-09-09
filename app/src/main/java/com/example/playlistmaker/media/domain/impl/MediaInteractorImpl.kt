package com.example.playlistmaker.media.domain.impl

import android.content.Context
import android.net.Uri
import com.example.playlistmaker.media.data.MediaRepository
import com.example.playlistmaker.media.domain.MediaInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.InputStream
import java.util.Date

class MediaInteractorImpl(private val repository: MediaRepository) : MediaInteractor {
    override fun getFavID(): List<Int> {
        return repository.getFavID()
    }

    override fun getTracks(): Flow<List<Track>> {
        return repository.getFavorites()
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override fun getPlaylist(id: Long): Flow<Playlist> {
        return repository.getPlaylist(id)
    }

    override fun getPlaylistTracks(): Flow<List<Track>>{
        return repository.getPlaylistTracks()
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

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override fun saveImage(
        context: Context,
        name: String,
        inputStream: InputStream?,
        time: Date
    ): Uri {
        return repository.saveImage(context, name, inputStream, time)
    }

    override suspend fun insertPlaylistTrack(track: Track) {
        repository.savePlaylistTrack(track)
    }
}