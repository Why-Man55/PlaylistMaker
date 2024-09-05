package com.example.playlistmaker.media.ui.playlistPlayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlaylistBinding
import com.example.playlistmaker.media.presentation.PlaylistActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistActivity : AppCompatActivity() {
    private val viewModel by viewModel<PlaylistActivityViewModel>()
    private var _binding: ActivityPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.returnPlaylist(intent).observe(this){
            val playlist = it
            binding.playlistNameView.text = playlist.name
            binding.playlistInfoView.text = playlist.info
            binding.playlistTrackCount.text = playlist.count.toString()
            Glide.with(this)
                .load(playlist.image)
                .transform(CenterCrop())
                .placeholder(R.drawable.empty_av)
                .into(binding.playlistImage)
        }

        binding.playlistBack.setOnClickListener{
            finish()
        }
    }
}