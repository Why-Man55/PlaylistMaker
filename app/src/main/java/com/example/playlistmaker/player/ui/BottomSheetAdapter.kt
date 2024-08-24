package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.SimplePlaylistViewBinding
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.ui.playlists.PlaylistItemComparator
import com.example.playlistmaker.player.domain.api.PlaylistOnClicked

class BottomSheetAdapter(private val playlistOnClicked: PlaylistOnClicked): ListAdapter<Playlist, BottomSheetViewHolder>(PlaylistItemComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return BottomSheetViewHolder(SimplePlaylistViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener{
            playlistOnClicked.saveIntoPlaylist(item)
        }
    }
}