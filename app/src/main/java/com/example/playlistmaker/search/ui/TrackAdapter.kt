package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.search.domain.api.TrackOnClicked
import com.example.playlistmaker.search.domain.models.Track


class TrackAdapter(
    context: Context,
    sp:SharedPreferences,
    private val trackOnClicked: TrackOnClicked
) : ListAdapter<Track,TrackViewHolder>(ItemComparator()) {

    private val searchHistory = Creator.provideTrackInteractor(sp,context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            searchHistory.saveTrack(item)
            trackOnClicked.getTrackAndStart(item)
        }
    }
}