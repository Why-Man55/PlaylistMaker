package com.example.playlistmaker.media.ui.playlistPlayer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlaylistBinding
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.domain.model.PlaylistLongClicked
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
    private lateinit var playlist: Playlist
    private lateinit var tracks: List<Track>
    private lateinit var trackDelete: String

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

        val emptyTracksMes = MaterialAlertDialogBuilder(this, R.style.dialog)
            .setMessage(getString(R.string.empty_tracks_message))

        val trackDeleteDialog = MaterialAlertDialogBuilder(this, R.style.dialog)
            .setTitle(getString(R.string.delete_track_title))
            .setMessage(getString(R.string.track_delete_message))
            .setNeutralButton(getString(R.string.cancel)) { _, _ ->
                // empty
            }
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.updatePlaylist(trackDelete)
                viewModel.bindAgain()
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

        val trackOnClicked = object : TrackOnClicked {
            override fun getTrackAndStart(track: Track) {
                if (clickDebounce()) {
                    val displayIntent = Intent(this@PlaylistActivity, PlayerActivity::class.java)
                    displayIntent.putExtra("track", Gson().toJson(track))
                    startActivity(displayIntent)
                }
            }
        }

        val playlistLongClick = object : PlaylistLongClicked {
            override fun deleteTrack(track: Track) {
                trackDelete = track.trackID.toString()
                trackDeleteDialog.show()
            }
        }

        val adapter = PlaylistActivityAdapter(trackOnClicked, playlistLongClick)
        adapter.submitList(listOf())
        binding.playlistSheetRv.adapter = adapter

        viewModel.returnPlaylist(intent).observe(this) {
            playlist = it.playlist
            tracks = it.tracks

            adapter.submitList(tracks.reversed())
            bindStates(it.playlist)

            binding.emptyPlaylistMessage.isVisible = tracks.isEmpty()
            binding.playlistTime.text = returnTime(it.tracksTime)
        }

        binding.playlistShareBut.setOnClickListener {
            if (tracks.isEmpty()) {
                emptyTracksMes.show()
            } else {
                viewModel.sharePlaylist()
            }
        }

        binding.playlistOptionsBut.setOnClickListener {
            optionsBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.optionShare.setOnClickListener {
            if (tracks.isEmpty()) {
                emptyTracksMes.show()
            } else {
                viewModel.sharePlaylist()
            }
        }

        binding.optionRename.setOnClickListener {
            val displayIntent = Intent(this, NewPlaylistActivity::class.java)
            displayIntent.putExtra("playlist", Gson().toJson(playlist))
            startActivity(displayIntent)
        }

        binding.optionDelete.setOnClickListener {
            deleteDialog.show()
        }

        binding.playlistBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.bindAgain()
    }

    private fun bindStates(playlist: Playlist) {
        binding.playlistInfoView.text = playlist.info
        binding.playlistTrackCount.text = returnCount(playlist.count)
        binding.playlistNameView.text = playlist.name
        binding.optionsName.text = playlist.name
        binding.optionsCount.text = returnCount(playlist.count)
        Glide.with(binding.root)
            .load(playlist.image)
            .transform(CenterCrop(), RoundedCorners(2))
            .placeholder(R.drawable.empty_av)
            .into(binding.optionsImage)
        Glide.with(binding.root).load(playlist.image)
            .transform(CenterCrop()).placeholder(
                R.drawable.empty_av
            ).into(binding.playlistImage)
    }

    private fun returnCount(count: Int): String {
        return when {
            count == 11 or 12 or 13 or 14 -> {
                "$count треков"
            }
            count % 10 == 1 -> {
                "$count трек"
            }
            (count % 10 == 2) or (count % 10 == 3) or (count % 10 == 4) -> {
                "$count трека"
            }
            else -> {
                "$count треков"
            }
        }
    }

    private fun returnTime(time: Long): String {
        val currentTime = SimpleDateFormat("mm", Locale.getDefault()).format(time).toInt()
        return if ((currentTime == 11) or (currentTime == 12) or (currentTime == 13) or (currentTime == 14)) {
            "$currentTime минут"
        } else if (currentTime % 10 == 1) {
            "$currentTime минута"
        } else if ((currentTime % 10 == 2) or (currentTime % 10 == 3) or (currentTime % 10 == 4)) {
            "$currentTime минуты"
        }else {
            "$currentTime минут"
        }
    }

    companion object {
        private const val CLICK_DELAY = 1000L
    }
}