package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.search.domain.api.TrackOnClicked
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.domain.impl.SearchHistoryReplmpl
import com.example.playlistmaker.search.presentation.SearchViewModel


class TrackAdapter(
    private val items: TrackResponse?,
    private val searchHistory: SearchViewModel,
    private val trackOnClicked: TrackOnClicked
) : RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackViewBinding.inflate(layoutInspector, parent, false))
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