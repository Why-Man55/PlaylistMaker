package com.example.playlistmaker.media.ui.playlists

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistViewBinding
import com.example.playlistmaker.media.domain.model.Playlist

class PlaylistFragmentViewHolder(private val binding: PlaylistViewBinding): RecyclerView.ViewHolder(binding.root) {

    private val radius: Float = 8 * itemView.resources.displayMetrics.density
    fun bind(model:Playlist) = with(binding){
        playlistTitleName.text = model.name
        playlistTrackCount.text = model.count.toString()
        Glide.with(itemView).load(model.image).centerCrop().transform(RoundedCorners(radius.toInt())).placeholder(
            R.drawable.empty_av
        ).into(playlistTitleImage)
    }
}