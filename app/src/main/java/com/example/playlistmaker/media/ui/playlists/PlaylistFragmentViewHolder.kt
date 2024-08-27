package com.example.playlistmaker.media.ui.playlists

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistViewBinding
import com.example.playlistmaker.media.domain.model.Playlist

class PlaylistFragmentViewHolder(private val binding: PlaylistViewBinding): RecyclerView.ViewHolder(binding.root) {

    private val radius: Float = 8 * itemView.resources.displayMetrics.density
    fun bind(model:Playlist) = with(binding){
        playlistTitleName.text = model.name
        playlistTrackCount.text = bindTracks(model.count)
        Glide.with(itemView).load(model.image).transform(CenterCrop(), RoundedCorners(radius.toInt())).placeholder(
            R.drawable.empty_av
        ).into(playlistTitleImage)
    }

    private fun bindTracks(count:Int):String{
        return if(count == 11 or 12 or 13 or 14){
            "$count треков"
        }
        else if(count % 10 == 1){
            "$count трек"
        }
        else if(count % 10 == 2 or 3 or 4){
            "$count трека"
        }
        else{
            "$count треков"
        }
    }
}