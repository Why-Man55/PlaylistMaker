package com.example.playlistmaker.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SimplePlaylistViewBinding
import com.example.playlistmaker.media.domain.model.Playlist

class BottomSheetViewHolder(private val binding:SimplePlaylistViewBinding):RecyclerView.ViewHolder(binding.root) {
    private val radius: Float = 2 * itemView.resources.displayMetrics.density
    fun bind(model:Playlist){
        binding.playlistName.text = model.name
        binding.playlistCount.text = bindTracks(model.count)
        Glide.with(itemView).load(model.image).transform(CenterCrop(), RoundedCorners(radius.toInt())).placeholder(
            R.drawable.empty_av
        ).into(binding.playlistUrl)
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