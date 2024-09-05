package com.example.playlistmaker.search.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(private val binding: TrackViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val radius: Float = 2 * itemView.resources.displayMetrics.density
    fun bind(model: Track) = with(binding) {
        name.text = model.trackNameItem
        artist.text = model.artistNameItem
        time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeItem)
        Glide.with(itemView).load(model.trackAvatarItem).centerCrop()
            .transform(RoundedCorners(radius.toInt())).placeholder(
            R.drawable.empty_av
        ).into(url)
    }
}