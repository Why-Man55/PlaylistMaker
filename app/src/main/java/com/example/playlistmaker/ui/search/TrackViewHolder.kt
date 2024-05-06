package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.doamin.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parentView.context).inflate(
    R.layout.track_view, parentView, false)) {

    private val name = itemView.findViewById<TextView>(R.id.name)
    private val artist = itemView.findViewById<TextView>(R.id.artist)
    private val time = itemView.findViewById<TextView>(R.id.time)
    private val url = itemView.findViewById<ImageView>(R.id.url)

    private val radius: Float = 2 * itemView.resources.displayMetrics.density
    fun bind(model: Track) {
        name.text = model.trackNameItem
        artist.text = model.artistNameItem
        time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeItem)
        Glide.with(itemView).load(model.trackAvatarItem).centerCrop().transform(RoundedCorners(radius.toInt())).placeholder(
            R.drawable.empty_av
        ).into(url)
    }
}