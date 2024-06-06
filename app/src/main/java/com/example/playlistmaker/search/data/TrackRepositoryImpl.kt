package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.SearchResponseElements
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient): TrackRepository {
    override fun searchTracks(expression: String): SearchResponseElements {
        val response = networkClient.doSearch(TrackSearchRequest(expression))
        val code = if((response as TrackResponse).results.isEmpty()){
            0
        }
        else
        {
            200
        }
        return if(response.searchState == 200){
            SearchResponseElements(response.results.map {
                Track(it.trackNameItem,it.artistNameItem, it.trackTimeItem, it.trackAvatarItem,
                    it.trackID, it.collectionName, it.rYear, it.genre, it.country, it.audioUrl) }, code)
        } else{
            SearchResponseElements(emptyList(), response.searchState)
        }
    }
}