package com.example.playlistmaker.settings.presentation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.data.dto.ThemeSave

class SettingsViewModel(private val themeSave: ThemeSave):ViewModel() {
    companion object{
        fun getViewModelFactory(sp: SharedPreferences): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(Creator.getThemeSave(sp)) as T
                }
            }
    }
    fun editSP(boolean: Boolean){
        themeSave.editSP(boolean)
    }
}