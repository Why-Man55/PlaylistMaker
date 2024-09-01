package com.example.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media.data.db.dao.PlaylistDao
import com.example.playlistmaker.media.data.db.dao.PlaylistsTracksDao
import com.example.playlistmaker.media.data.db.dao.TrackDao
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.data.db.entity.PlaylistsTrackEntity
import com.example.playlistmaker.media.data.db.entity.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistsTrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playlistTrackDao(): PlaylistsTracksDao
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}