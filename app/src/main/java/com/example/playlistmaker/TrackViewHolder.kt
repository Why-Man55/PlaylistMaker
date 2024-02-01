package com.example.playlistmaker

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parentView.context).inflate(R.layout.track_view, parentView, false)) {

    private val name = itemView.findViewById<TextView>(R.id.name)
    private val artist = itemView.findViewById<TextView>(R.id.artist)
    private val time = itemView.findViewById<TextView>(R.id.time)
    private val url = itemView.findViewById<ImageView>(R.id.url)

    private val radius: Float = 2 * itemView.resources.displayMetrics.density
    fun bind(model: Track) {
        Log.d("MY_TAG", "bind model=$model")
        name.text = model.trackName
        artist.text = model.trackArtist
        time.text = model.trackTime
        Glide.with(itemView).load(model.trackAvatar).centerCrop().transform(RoundedCorners(radius.toInt())).placeholder(R.drawable.empty_av).into(url)
    }
}