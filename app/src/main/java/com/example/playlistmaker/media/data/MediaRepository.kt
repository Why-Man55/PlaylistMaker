package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getFavorites(): Flow<List<Track>>
    suspend fun changeFavorites(track: Track)
    suspend fun deleteTrack(track: Track)
}