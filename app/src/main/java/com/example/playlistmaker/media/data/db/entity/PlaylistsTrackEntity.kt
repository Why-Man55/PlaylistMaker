package com.example.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_track_table")
class PlaylistsTrackEntity(
    @PrimaryKey(autoGenerate = true)
    val idKey: Long,
    val id: Int,
    val trackNameItem: String,
    val artistNameItem: String,
    val trackTimeItem: Int,
    val trackAvatarItem: String,
    val collectionName: String,
    val rYear: String?,
    val genre: String,
    val country: String,
    val audioUrl: String?
)