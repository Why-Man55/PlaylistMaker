package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.TrackRepository
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient): TrackRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doSearch(TrackSearchRequest(expression))
        return when (response.searchState) {
            -1 -> {
                Resource.Error(-1)
            }
            200 -> {
            Resource.Success((response as TrackResponse).results.map {
                Track(it.trackNameItem,it.artistNameItem, it.trackTimeItem, it.trackAvatarItem,
                    it.trackID, it.collectionName, it.rYear, it.genre, it.country, it.audioUrl) })
            }
            else -> {
                Resource.Error(400)
            }
        }
    }
}