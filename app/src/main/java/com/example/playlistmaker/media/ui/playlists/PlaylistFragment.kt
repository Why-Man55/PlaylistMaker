package com.example.playlistmaker.media.ui.playlists

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.databinding.PlaylistsFragmentBinding
import com.example.playlistmaker.media.domain.api.PlaylistOpen
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.presentation.PlaylistsFragmentViewModel
import com.example.playlistmaker.media.ui.playlistPlayer.PlaylistActivity
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    companion object {
        fun newInstance(): Fragment = PlaylistFragment().apply {}
        private const val CLICK_DELAY = 1000L
    }

    private val viewModel by viewModel<PlaylistsFragmentViewModel>()
    private var _binding: PlaylistsFragmentBinding? = null
    private val binding get() = _binding!!

    private var isClickAllowed = true

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private val playlistOpen = object : PlaylistOpen {
        override fun startPlaylist(playlist: Playlist) {
            if (clickDebounce()) {
                val displayIntent = Intent(requireContext(), PlaylistActivity::class.java)
                displayIntent.putExtra("playlist", Gson().toJson(playlist))
                startActivity(displayIntent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PlaylistFragmentAdapter(playlistOpen)
        adapter.submitList(listOf())
        viewModel.returnPlaylists().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.playlistList.layoutManager = GridLayoutManager(requireActivity(), 2)
            binding.playlistList.adapter = adapter
            if (it.isEmpty()) {
                setError(true)
            } else {
                setError(false)
            }
        }

        binding.newPlaylistBut.setOnClickListener {
            val displayIntent = Intent(requireContext(), NewPlaylistActivity::class.java)
            displayIntent.putExtra("playlist", "")
            startActivity(displayIntent)
        }
        viewModel.getPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setError(boolean: Boolean) {
        binding.playlistErrorMassage.isVisible = boolean
    }
}