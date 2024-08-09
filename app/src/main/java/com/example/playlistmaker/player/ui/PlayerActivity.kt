package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.media.ui.FavoritesFragment
import com.example.playlistmaker.player.presentation.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

        viewModel.getTrack(intent).observe(this){
            bindTime(it.time)
            thisTrack = it.track
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
            lifecycleScope.launch(Dispatchers.IO) {
                if(viewModel.checkLiked(thisTrack.trackID)){
                    binding.playerLovedBut.setBackgroundResource(R.drawable.ic_active_fav_but)
                }
                else{
                    binding.playerLovedBut.setBackgroundResource(R.drawable.ic_loved_but)
                }
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
            viewModel.isFavClicked(thisTrack.isFavorite)
            FavoritesFragment().isChanged = true
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