package com.example.playlistmaker.media.data.converters

import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.data.db.entity.PlaylistsTrackEntity
import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track

class TrackConvertor {
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            0L,
            track.trackID,
            track.trackNameItem,
            track.artistNameItem,
            track.trackTimeItem,
            track.trackAvatarItem,
            track.collectionName,
            track.rYear,
            track.genre,
            track.country,
            track.audioUrl
        )
    }

    fun map(trackEntity: TrackEntity): Track {
        return Track(
            trackEntity.trackNameItem,
            trackEntity.artistNameItem,
            trackEntity.trackTimeItem,
            trackEntity.trackAvatarItem,
            trackEntity.id,
            trackEntity.collectionName,
            trackEntity.rYear,
            trackEntity.genre,
            trackEntity.country,
            trackEntity.audioUrl
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            0L,
            playlist.name,
            playlist.image,
            playlist.count,
            playlist.info,
            playlist.content
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.name,
            playlist.image,
            playlist.count,
            playlist.info,
            playlist.content,
            playlist.idKey
        )
    }

    fun mapUpdate(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.image,
            playlist.count,
            playlist.info,
            playlist.content
        )
    }

    fun mapPTE(track: Track): PlaylistsTrackEntity {
        return PlaylistsTrackEntity(
            0L,
            track.trackID,
            track.trackNameItem,
            track.artistNameItem,
            track.trackTimeItem,
            track.trackAvatarItem,
            track.collectionName,
            track.rYear,
            track.genre,
            track.country,
            track.audioUrl
        )
    }

}