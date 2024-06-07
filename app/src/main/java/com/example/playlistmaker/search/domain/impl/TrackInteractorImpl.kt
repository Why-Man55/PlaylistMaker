package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.data.TrackRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: TrackInteractor.TracksConsumer) {
        executor.execute {
            when(val resource = repository.searchTracks(expression)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
        }
        }
    }

    override fun getHistory(): List<Track> {
        return repository.getHistory()
    }

    override fun saveTrack(track: Track) {
        repository.saveTrack(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}