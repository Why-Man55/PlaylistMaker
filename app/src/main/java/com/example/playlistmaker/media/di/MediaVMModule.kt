package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.presentation.FavoritesFragmentViewModel
import com.example.playlistmaker.media.presentation.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaViewModule = module{
    viewModel {
        FavoritesFragmentViewModel()
    }
    viewModel {
        PlaylistsFragmentViewModel()
    }
}