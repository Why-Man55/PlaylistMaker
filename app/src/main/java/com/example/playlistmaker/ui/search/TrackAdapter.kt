package com.example.playlistmaker.ui.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.doamin.api.TrackOnClicked
import com.example.playlistmaker.data.dto.TrackResponse


class TrackAdapter(
    private val items: TrackResponse?,
    private val searchHistory: SearchHistory,
    private val trackOnClicked: TrackOnClicked
) : RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(items!!.results[position])
        holder.itemView.setOnClickListener {
            searchHistory.save(items.results[position])
            trackOnClicked.getTrackAndStart(items.results[position])
        }
    }

    override fun getItemCount(): Int {
        return items!!.resultCount
    }
}