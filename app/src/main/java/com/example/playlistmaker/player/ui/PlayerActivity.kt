package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.presentation.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity()  {

    private val viewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: ActivityPlayerBinding

    private lateinit var thisTrack:Track

    private val radius: Float by lazy {
        8 * this.resources.displayMetrics.density
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var favotiteSelected = false

        viewModel.getTrack(intent).observe(this){
            bindTime(it.time)
            thisTrack = it.track
            favotiteSelected = thisTrack.isFavorite
            bindStaticViews(thisTrack)
            bindGlide(thisTrack)
            binding.playerLengthEmpty.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                thisTrack.trackTimeItem
            )
            if(thisTrack.collectionName.isEmpty()){
                bindAlbumVisible(false)
            }
            else {
                bindAlbumVisible(true)
                binding.playerAlbumEmpty.text = thisTrack.collectionName
            }
            if(favotiteSelected){
                bindFavBut(true)
            }
        }

        viewModel.getReadyMedia()

        viewModel.setOnPreparedListener{
            binding.playerPlayBut.isEnabled = true
        }

        viewModel.setOnCompletionListener {
            setPlay()
            callBack()
            bindTime(0)
        }

        binding.playerPlayBut.setOnClickListener {
            playbackControl()
        }

        binding.playerBack.setOnClickListener {
            finish()
        }

        binding.playerLovedBut.setOnClickListener{
            if(favotiteSelected){
                bindFavBut(false)
                viewModel.changeFavorites(thisTrack)
            }
            else{
                bindFavBut(true)
                viewModel.deleteTrack(thisTrack)
            }
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

    private fun bindFavBut(boolean: Boolean){
        if(boolean){
            binding.playerLovedBut.setImageResource(R.drawable.selected_favorite_icon)
        }
        else{
            binding.playerLovedBut.setImageResource(R.drawable.ic_loved_but)
        }
    }

    private fun bindStaticViews(track: Track){
        binding.playerTitleName.text = track.trackNameItem
        binding.playerArtistName.text = track.artistNameItem
        binding.playerYearEmpty.text = track.rYear?.replaceAfter('-', "")?.substring(0, 4)
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
        viewModel.stopTimer()
    }
}