package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("Select * FROM playlists_table ORDER BY idKey")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("Select * FROM playlists_table WHERE idKey = :id")
    fun getPlaylist(id:Long): Flow<PlaylistEntity>
}