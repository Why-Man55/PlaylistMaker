package com.example.playlistmaker.settings.presentation

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.settings.domain.SettingInteractor
import com.example.playlistmaker.settings.domain.models.ThemeSettings
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingInteractor
):ViewModel() {

    fun editSP(boolean: Boolean){
        settingsInteractor.updateThemeSetting(ThemeSettings(boolean))
    }
    fun startShare(){
        sharingInteractor.shareApp()
    }
    fun startSupport(){
        sharingInteractor.openSupport()
    }
    fun startAgreement(){
        sharingInteractor.openTerms()
    }


    companion object{
        fun getViewModelFactory(context: Context,sp:SharedPreferences): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(Creator.getSharingInt(context), Creator.provideSettingsInteractor(sp)) as T
                }
            }
    }
}