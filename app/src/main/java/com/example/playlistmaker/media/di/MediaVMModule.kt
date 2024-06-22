package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.presentation.FavoritesFragmentVM
import com.example.playlistmaker.media.presentation.PlaylistsFragmentVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaViewModule = module{
    viewModel {
        FavoritesFragmentVM()
    }
    viewModel {
        PlaylistsFragmentVM()
    }
}