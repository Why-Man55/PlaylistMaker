package com.example.playlistmaker.media.data.impl

import com.example.playlistmaker.media.data.MediaRepository
import com.example.playlistmaker.media.data.converters.TrackConvertor
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: TrackConvertor): MediaRepository {

    override fun getFavID(): List<Int> {
        return appDatabase.trackDao().getTrackID()
    }
    override fun getFavorites(): Flow<List<Track>> {
        return appDatabase.trackDao().getTracks().map{entityList -> entityList.map(converter::map)}
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists().map { playlist -> playlist.map(converter::map)}
    }

    override suspend fun deleteTrack(track: Track){
        appDatabase.trackDao().deleteTrack(converter.map(track).id )
    }

    override suspend fun changeFavorites(track: Track) {
        appDatabase.trackDao().insertTrack(converter.map(track))
    }

    override suspend fun insertPlaylists(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(converter.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(converter.map(playlist))
    }
 }