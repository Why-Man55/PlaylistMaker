package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getFavID(): List<Int>
    fun getFavorites(): Flow<List<Track>>
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun changeFavorites(track: Track)
    suspend fun deleteTrack(track: Track)
    suspend fun updatePlaylists(playlist: Playlist)
}