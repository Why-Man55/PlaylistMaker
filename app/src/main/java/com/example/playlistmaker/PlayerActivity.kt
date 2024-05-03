package com.example.playlistmaker

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
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
    private lateinit var playBut: ImageButton
    private lateinit var playTime: TextView

    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())

    private var playerState = STATE_DEFAULT

    private val radius: Float by lazy {
        8 * this.resources.displayMetrics.density
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val track: Track = Gson().fromJson(intent.extras?.getString("track"), Track::class.java)

        titleName = findViewById(R.id.player_title_name)
        artistName = findViewById(R.id.player_artist_name)
        playerImg = findViewById(R.id.player_img)

        trackLength = findViewById(R.id.player_length_empty)
        trackAlbum = findViewById(R.id.player_album_empty)
        trackYear = findViewById(R.id.player_year_empty)
        trackStyle = findViewById(R.id.player_style_empty)
        trackCountry = findViewById(R.id.player_country_empty)

        playBut = findViewById(R.id.player_play_but)
        staticTrackAlbum = findViewById(R.id.player_album)
        playTime = findViewById(R.id.play_timer)
        val backButton = findViewById<Button>(R.id.player_back)

        mediaPlayer.setDataSource(track.audioUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playBut.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            setPlay()
            playerState = STATE_PREPARED
            handlerCallBack()
            playTime.text = getString(R.string.player_time_empty)
        }

        playBut.setOnClickListener {
            playbackControl()
        }

        backButton.setOnClickListener {
            finish()
        }

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
        trackYear.text = track.rYear.replaceAfter('-', "").substring(0, 4)
        trackStyle.text = track.genre
        trackCountry.text = track.country
    }
    private fun runTime(): Runnable{
        return object : Runnable {
            override fun run() {
                if(playerState == STATE_PLAYING){
                    playTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, SET_TIME_WAIT)
                }
            }
        }
    }
    private fun setPlay(){
        playBut.setBackgroundResource(R.drawable.ic_play_but)
    }

    private fun setPause(){
        playBut.setBackgroundResource(R.drawable.ic_pause_but)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handlerCallBack()
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        setPause()
        playerState = STATE_PLAYING
        handler.post(runTime())
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        setPlay()
        playerState = STATE_PAUSED
        handlerCallBack()
    }

    private fun handlerCallBack(){
        handler.removeCallbacks(runTime())
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val SET_TIME_WAIT = 400L
    }
}