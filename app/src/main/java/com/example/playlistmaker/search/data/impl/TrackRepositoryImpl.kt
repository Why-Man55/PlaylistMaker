package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.TrackRepository
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient): TrackRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doSearch(TrackSearchRequest(expression))
        when (response.searchState) {
            -1 -> {
                emit(Resource.Error(-1))
            }
            200 -> {
            with(response as TrackResponse) {
                val data = results.map{
                    Track(it.trackNameItem,it.artistNameItem, it.trackTimeItem, it.trackAvatarItem,
                        it.trackID, it.collectionName, it.rYear, it.genre, it.country, it.audioUrl)
                }
                emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error(400))
            }
        }
    }
}