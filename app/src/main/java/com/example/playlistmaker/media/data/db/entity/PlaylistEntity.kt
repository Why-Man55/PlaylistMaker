package com.example.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val idKey: Long,
    val name: String,
    val image: String?,
    val count: Int,
    val info: String?,
    val content: String
)