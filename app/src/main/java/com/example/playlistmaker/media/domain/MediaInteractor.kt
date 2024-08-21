package com.example.playlistmaker.media.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface MediaInteractor {
    fun getFavID(): List<Int>
    fun getTracks(): Flow<List<Track>>
    suspend fun changeFavorites(track: Track)
    suspend fun deleteTrack(track: Track)
}