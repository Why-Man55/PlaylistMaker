package com.example.playlistmaker.media.ui.playlistPlayer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlaylistBinding
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.presentation.PlaylistActivityViewModel
import com.example.playlistmaker.media.ui.playlists.NewPlaylistActivity
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.api.TrackOnClicked
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistActivity : AppCompatActivity() {
    private val viewModel by viewModel<PlaylistActivityViewModel>()
    private var _binding: ActivityPlaylistBinding? = null
    private val binding get() = _binding!!

    private var isClickAllowed = true
    private lateinit var playlist:Playlist

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tracksBottomSheet = BottomSheetBehavior.from(binding.playlistBottomSheet)
        tracksBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED

        val optionsBottomSheet = BottomSheetBehavior.from(binding.playlistOptionsBottomSheet)
        optionsBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN

        val trackOnClicked = object : TrackOnClicked {
            override fun getTrackAndStart(track: Track) {
                if (clickDebounce()) {
                    val displayIntent = Intent(this@PlaylistActivity, PlayerActivity::class.java)
                    displayIntent.putExtra("track", Gson().toJson(track))
                    startActivity(displayIntent)
                }
            }
        }

        val deleteDialog = MaterialAlertDialogBuilder(this, R.style.dialog)
            .setTitle(getString(R.string.delete_title))
            .setMessage(getString(R.string.delete_text))
            .setNegativeButton(getString(R.string.no)) { _, _ ->
                // empty
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deletePlaylist()
                finish()
            }

        val adapter = PlaylistActivityAdapter(trackOnClicked)
        adapter.submitList(listOf())
        binding.playlistSheetRv.adapter = adapter

        viewModel.returnPlaylist(intent).observe(this){
            playlist = it.playlist

            adapter.submitList(it.tracks)
            bindStates(it.playlist)
            binding.playlistTime.text = SimpleDateFormat("mm", Locale.getDefault()).format(it.tracksTime)
            Glide.with(this)
                .load(playlist.image)
                .transform(CenterCrop())
                .placeholder(R.drawable.empty_av)
                .into(binding.playlistImage)
        }

        binding.playlistShareBut.setOnClickListener {
            viewModel.sharePlaylist()
        }

        binding.playlistOptionsBut.setOnClickListener{
            optionsBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.optionShare.setOnClickListener{
            viewModel.sharePlaylist()
        }

        binding.optionRename.setOnClickListener {
            val displayIntent = Intent(this, NewPlaylistActivity::class.java)
            displayIntent.putExtra("playlist", Gson().toJson(playlist))
            startActivity(displayIntent)
        }

        binding.optionDelete.setOnClickListener {
            deleteDialog.show()
        }

        binding.playlistBack.setOnClickListener{
            finish()
        }
    }

    private fun bindStates(playlist: Playlist){
        binding.playlistNameView.text = playlist.name
        binding.playlistInfoView.text = playlist.info
        binding.playlistTrackCount.text = playlist.count.toString()
        binding.optionsName.text = playlist.name
        binding.optionsCount.text = playlist.count.toString()
        Glide.with(this)
            .load(playlist.image)
            .transform(CenterCrop(), RoundedCorners(2))
            .placeholder(R.drawable.empty_av)
            .into(binding.optionsImage)
    }

    companion object{
        private const val CLICK_DELAY = 1000L
    }
}