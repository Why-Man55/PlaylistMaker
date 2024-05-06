package com.example.playlistmaker.ui.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.doamin.api.TrackOnClicked
import com.example.playlistmaker.doamin.models.Track

class HistoryAdapter(private val tracks: List<Track>?,
                     private val trackOnClicked: TrackOnClicked
) : RecyclerView.Adapter<TrackViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks!![position])
        holder.itemView.setOnClickListener {
            trackOnClicked.getTrackAndStart(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks!!.count()
    }
}