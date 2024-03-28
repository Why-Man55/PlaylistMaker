package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var titleName: TextView
    private lateinit var artistName: TextView
    private lateinit var playerImg: ImageView
    private lateinit var trackLength: TextView
    private lateinit var trackAlbum: TextView
    private lateinit var trackYear: TextView
    private lateinit var trackStyle: TextView
    private lateinit var trackCountry: TextView
    private lateinit var staticTrackAlbum: TextView

    private val radius: Float = 8 * resources.displayMetrics.density
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        titleName = findViewById(R.id.player_title_name)
        artistName = findViewById(R.id.player_artist_name)
        playerImg = findViewById(R.id.player_img)

        trackLength = findViewById(R.id.player_length_empty)
        trackAlbum = findViewById(R.id.player_album_empty)
        trackYear = findViewById(R.id.player_year_empty)
        trackStyle = findViewById(R.id.player_style_empty)
        trackCountry = findViewById(R.id.player_country_empty)

        val backButton = findViewById<Button>(R.id.player_back)

        backButton.setOnClickListener {
            finish()
        }
    }

    fun bind(track: Track){
        titleName.text = track.trackNameItem
        artistName.text = track.artistNameItem
        Glide.with(this)
            .load(track.trackAvatarItem.replaceAfterLast('/',"512x512bb.jpg"))
            .centerCrop()
            .transform(RoundedCorners(radius.toInt()))
            .placeholder(R.drawable.empty_av)
            .into(playerImg)
        trackLength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeItem)
        if(track.collectionName.isEmpty()){
            trackAlbum.visibility = View.GONE
            staticTrackAlbum.visibility = View.GONE
        }
        else{
            trackAlbum.visibility = View.VISIBLE
            staticTrackAlbum.visibility = View.VISIBLE
            trackAlbum.text = track.collectionName
        }
        trackYear.text = track.rDate.toString()
        trackStyle.text = track.genre
        trackCountry.text = track.country
    }
}