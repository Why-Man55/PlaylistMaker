package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.presentation.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : ComponentActivity()  {

    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: ActivityPlayerBinding

    private fun runTime(): Runnable{
        return Runnable {
            if(playerState == STATE_PLAYING){
                binding.playTimer.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(viewModel.returnCurrentPosition())
                viewModel.handlerPostDelayed(SET_TIME_WAIT)
            }
        }
    }

    private var playerState = STATE_DEFAULT

    private val radius: Float by lazy {
        8 * this.resources.displayMetrics.density
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val track: Track = Gson().fromJson(intent.extras?.getString("track"), Track::class.java)

        viewModel = ViewModelProvider(this, PlayerViewModel.getViewModelFactory(track.audioUrl, runTime()))[PlayerViewModel::class.java]

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getReadyMedia()
        viewModel.playerInter.mediaPlayer.setOnPreparedListener{
            binding.playerPlayBut.isEnabled = true
            playerState = STATE_PREPARED
        }
        viewModel.playerInter.mediaPlayer.setOnCompletionListener {
            setPlay()
            playerState = STATE_PREPARED
            viewModel.handlerCallBack()
            binding.playTimer.text = getString(R.string.player_time_empty)
        }

        binding.playerPlayBut.setOnClickListener {
            playbackControl()
        }

        binding.playerBack.setOnClickListener {
            finish()
        }

        binding.playerTitleName.text = track.trackNameItem
        binding.playerArtistName.text = track.artistNameItem
        Glide.with(this)
            .load(track.trackAvatarItem.replaceAfterLast('/',"512x512bb.jpg"))
            .centerCrop()
            .transform(RoundedCorners(radius.toInt()))
            .placeholder(R.drawable.empty_av)
            .into(binding.playerImg)
        binding.playerLengthEmpty.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeItem)
        if(track.collectionName.isEmpty()){
            binding.playerAlbumEmpty.visibility = View.GONE
            binding.playerAlbum.visibility = View.GONE
        }
        else{
            binding.playerAlbumEmpty.visibility = View.VISIBLE
            binding.playerAlbum.visibility = View.VISIBLE
            binding.playerAlbum.text = track.collectionName
        }
        binding.playerYearEmpty.text = track.rYear.replaceAfter('-', "").substring(0, 4)
        binding.playerStyleEmpty.text = track.genre
        binding.playerCountryEmpty.text = track.country
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
        setPlay()
        callBack()
        playerState = STATE_PAUSED
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.playRelease()
        callBack()
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                viewModel.pausePlayer()
                setPlay()
                playerState = STATE_PAUSED
            }
            STATE_PREPARED, STATE_PAUSED -> {
                viewModel.startPlayer()
                setPause()
                playerState = STATE_PLAYING
                viewModel.handlerPost()
            }
        }
    }

    private fun setPlay(){
        binding.playerPlayBut.setBackgroundResource(R.drawable.ic_play_but)
    }

    private fun setPause(){
        binding.playerPlayBut.setBackgroundResource(R.drawable.ic_pause_but)
    }

    private fun callBack(){
        viewModel.handlerCallBack()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val SET_TIME_WAIT = 400L
    }
}