package com.example.playlistmaker.media.ui.playlists

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.media.domain.model.Playlist

class PlaylistItemComparator : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem == newItem
    }
}