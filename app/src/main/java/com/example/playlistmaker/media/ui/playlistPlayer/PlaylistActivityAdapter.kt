package com.example.playlistmaker.media.ui.playlistPlayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.media.domain.model.PlaylistLongClicked
import com.example.playlistmaker.search.domain.api.TrackOnClicked
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TrackItemComparator
import com.example.playlistmaker.search.ui.TrackViewHolder

class PlaylistActivityAdapter(
    private val trackOnClicked: TrackOnClicked,
    private val trackLongClicked: PlaylistLongClicked
) : ListAdapter<Track, TrackViewHolder>(TrackItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            trackOnClicked.getTrackAndStart(item)
        }
        holder.itemView.setOnLongClickListener{
            trackLongClicked.deleteTrack(item)
            true
        }
    }
}