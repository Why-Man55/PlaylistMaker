package com.example.playlistmaker.search.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.search.domain.models.Track

class TrackItemComparator: DiffUtil.ItemCallback<Track>(){
    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }

}