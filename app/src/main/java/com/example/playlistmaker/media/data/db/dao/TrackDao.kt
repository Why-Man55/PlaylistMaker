package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("DELETE FROM track_table WHERE id = :trackId")
    fun deleteTrack (trackId:Int)

    @Query("Select * FROM track_table ORDER BY idKey DESC")
    fun getTracks(): Flow<List<TrackEntity>>

    @Query("Select id FROM track_table")
    fun getTrackID(): List<Int>
}