package com.example.playlistmaker.media.domain

import android.content.Context
import android.net.Uri
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import java.io.InputStream
import java.util.Date

interface MediaInteractor {
    fun getFavID(): List<Int>
    fun getTracks(): Flow<List<Track>>
    fun getPlaylists():Flow<List<Playlist>>
    suspend fun changeFavorites(track: Track)
    suspend fun deleteTrack(track: Track)
    suspend fun insertPlaylists(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    fun saveImage(context: Context, name: String, inputStream: InputStream?, time: Date):Uri
    suspend fun loadImage(context: Context, time:Date, name: String): Uri
    suspend fun insertPlaylistTrack(track: Track)
}