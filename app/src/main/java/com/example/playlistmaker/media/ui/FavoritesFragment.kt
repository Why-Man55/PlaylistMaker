package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FavoritesFragmentBinding
import com.example.playlistmaker.media.presentation.FavoritesFragmentVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment:Fragment() {
    companion object{
        fun newInstance() = FavoritesFragment().apply { }
    }

    private val viewModel by viewModel<FavoritesFragmentVM>()
    private lateinit var binding: FavoritesFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FavoritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}