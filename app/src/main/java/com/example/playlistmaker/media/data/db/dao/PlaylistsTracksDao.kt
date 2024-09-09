package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.db.entity.PlaylistsTrackEntity
import com.example.playlistmaker.media.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistsTracksDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrack(trackEntity: PlaylistsTrackEntity)

    @Query("Select * FROM playlist_track_table")
    fun getPlaylistTracks(): Flow<List<TrackEntity>>
}