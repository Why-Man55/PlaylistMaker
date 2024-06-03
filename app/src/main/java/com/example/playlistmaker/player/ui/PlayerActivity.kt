package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
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

class PlayerActivity : AppCompatActivity()  {

    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: ActivityPlayerBinding

    private val radius: Float by lazy {
        8 * this.resources.displayMetrics.density
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        viewModel = ViewModelProvider(this, PlayerViewModel.getViewModelFactory())[PlayerViewModel::class.java]
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getPlayerStates().observe(this){
            time -> bindTime(time)
        }

        viewModel.getTrack(intent).observe(this){
            trackRet ->
            bindStaticViews(trackRet)
            bindGlide(trackRet)
            binding.playerLengthEmpty.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                trackRet.trackTimeItem
            )
            if(trackRet.collectionName.isEmpty()){
                bindAlbumVisible(false)
            }
            else {
                bindAlbumVisible(true)
                binding.playerAlbumEmpty.text = trackRet.collectionName
            }
        }

        viewModel.getReadyMedia()

        viewModel.setOnPreparedListener{
            binding.playerPlayBut.isEnabled = true

        }

        viewModel.setOnCompletionListener {
            setPlay()
            viewModel.handlerCallBack()
            binding.playTimer.text = getString(R.string.player_time_empty)
        }

        binding.playerPlayBut.setOnClickListener {
            playbackControl()
        }

        binding.playerBack.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
        setPlay()
        callBack()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.playRelease()
        callBack()
    }

    private fun playbackControl() {
        if(viewModel.isPlaying()){
            setPlay()
        }
        else{
            setPause()
        }
    }

    private fun bindTime(time: Long){
        binding.playTimer.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }

    private fun bindStaticViews(track: Track){
        binding.playerTitleName.text = track.trackNameItem
        binding.playerArtistName.text = track.artistNameItem
        binding.playerYearEmpty.text = track.rYear.replaceAfter('-', "").substring(0, 4)
        binding.playerStyleEmpty.text = track.genre
        binding.playerCountryEmpty.text = track.country
    }

    private fun bindGlide(track: Track){
        Glide.with(this)
            .load(track.trackAvatarItem.replaceAfterLast('/',"512x512bb.jpg"))
            .centerCrop()
            .transform(RoundedCorners(radius.toInt()))
            .placeholder(R.drawable.empty_av)
            .into(binding.playerImg)
    }

    private fun bindAlbumVisible(boolean: Boolean){
        if(boolean){
            binding.playerAlbumEmpty.visibility = View.VISIBLE
            binding.playerAlbum.visibility = View.VISIBLE
        }
        else{
            binding.playerAlbumEmpty.visibility = View.GONE
            binding.playerAlbum.visibility = View.GONE
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
}