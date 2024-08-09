package com.example.playlistmaker.media.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.databinding.FavoritesFragmentBinding
import com.example.playlistmaker.media.presentation.FavoritesFragmentViewModel
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.api.TrackOnClicked
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment:Fragment() {
    companion object{
        fun newInstance() = FavoritesFragment().apply { }
        private const val CLICK_DELAY:Long = 1000
        private const val INTENT_KEY = "track"
    }

    private val viewModel by viewModel<FavoritesFragmentViewModel>()
    private var _binding: FavoritesFragmentBinding? = null
    private val binding get() = _binding!!

    private var isClickAllowed = true

    var isChanged = false

    private fun clickDebounce() : Boolean {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FavoritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actContext = requireContext()
        val trackOnClicked = object :TrackOnClicked{
            override fun getTrackAndStart(track: Track) {
                if(clickDebounce()){
                    val displayIntent = Intent(actContext, PlayerActivity::class.java)
                    displayIntent.putExtra(INTENT_KEY,Gson().toJson(track))
                    startActivity(displayIntent)
                }
            }
        }

        val favoriteAdapter = TrackAdapter(trackOnClicked)
        favoriteAdapter.submitList(listOf())

        viewModel.getTracks()

        viewModel.getFavorites().observe(this as LifecycleOwner){
            if(it.error != 1){
                favoriteAdapter.submitList(it.tracks)
                binding.favoriteError.isVisible = false
            }
            else{
                favoriteAdapter.submitList(listOf())
                binding.favoriteError.isVisible = true
            }
            binding.favoriteRv.adapter = favoriteAdapter
        }
    }

    override fun onStart() {
        super.onStart()

        if(isChanged){
            viewModel.getTracks()
            isChanged = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}