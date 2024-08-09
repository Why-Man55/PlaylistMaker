package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.data.TrackRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {

    override fun searchTrack(expression: String): Flow<Pair<List<Track>?, Int?>> {
        return repository.searchTracks(expression).map { res ->
            when(res) {
                is Resource.Success -> { Pair(res.data, null) }
                is Resource.Error -> { Pair(null, res.message)}
        }
        }
    }
}