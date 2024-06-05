package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.TrackOnClicked
import com.example.playlistmaker.search.domain.models.Track


class TrackAdapter(
    private val items: List<Track>?,
    private val searchHistory: SearchHistoryRepository,
    private val trackOnClicked: TrackOnClicked
) : ListAdapter<Track,TrackViewHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(items!![position])
        holder.itemView.setOnClickListener {
            searchHistory.save(items[position])
            trackOnClicked.getTrackAndStart(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items!!.size
    }
}