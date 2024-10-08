package com.example.playlistmaker.media.ui.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.PlaylistViewBinding
import com.example.playlistmaker.media.domain.model.Playlist

class PlaylistFragmentAdapter :
    ListAdapter<Playlist, PlaylistFragmentViewHolder>(PlaylistItemComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistFragmentViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistFragmentViewHolder(
            PlaylistViewBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlaylistFragmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}