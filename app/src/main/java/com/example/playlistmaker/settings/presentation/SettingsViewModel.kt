package com.example.playlistmaker.settings.presentation

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingInteractor
import com.example.playlistmaker.settings.domain.models.ThemeSettings
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingInteractor
) : ViewModel() {

    fun editSP(boolean: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(boolean))
    }

    fun startShare() {
        sharingInteractor.shareApp()
    }

    fun startSupport() {
        sharingInteractor.openSupport()
    }

    fun startAgreement() {
        sharingInteractor.openTerms()
    }
}