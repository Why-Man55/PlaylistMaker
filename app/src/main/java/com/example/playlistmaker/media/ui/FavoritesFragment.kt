package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FavoritesFragmentBinding
import com.example.playlistmaker.media.presentation.FavoritesFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment:Fragment() {
    companion object{
        fun newInstance() = FavoritesFragment().apply { }
    }

    private val viewModel by viewModel<FavoritesFragmentViewModel>()
    private var _binding: FavoritesFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FavoritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}