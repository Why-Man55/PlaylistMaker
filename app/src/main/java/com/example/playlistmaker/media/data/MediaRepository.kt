package com.example.playlistmaker.media.data

import android.content.Context
import android.net.Uri
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import java.io.InputStream
import java.util.Date

interface MediaRepository {
    fun getFavID(): List<Int>
    fun getFavorites(): Flow<List<Track>>
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun changeFavorites(track: Track)
    suspend fun deleteTrack(track: Track)
    suspend fun insertPlaylists(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    fun saveImage(context: Context, name: String, inputStream: InputStream?, time: Date)
    suspend fun loadImage(context: Context, time:Date, name: String): Uri
    suspend fun savePlaylistTrack(track: Track)
}