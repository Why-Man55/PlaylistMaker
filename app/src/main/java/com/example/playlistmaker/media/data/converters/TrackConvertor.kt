package com.example.playlistmaker.media.data.converters

import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track

class TrackConvertor {
    fun map(track: Track):TrackEntity{
        return TrackEntity(track.trackID,
            track.trackNameItem,
            track.artistNameItem,
            track.trackTimeItem,
            track.trackAvatarItem,
            track.collectionName,
            track.rYear,
            track.genre,
            track.country,
            track.audioUrl,
            track.isFavorite)
    }

    fun map(trackEntity: TrackEntity): Track {
        return Track(trackEntity.trackNameItem,
            trackEntity.artistNameItem,
            trackEntity.trackTimeItem,
            trackEntity.trackAvatarItem,
            trackEntity.id,
            trackEntity.collectionName,
            trackEntity.rYear, trackEntity.genre,
            trackEntity.country,
            trackEntity.audioUrl)
    }

}