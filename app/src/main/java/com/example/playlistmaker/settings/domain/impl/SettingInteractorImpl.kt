package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.App
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.domain.SettingInteractor
import com.example.playlistmaker.settings.domain.models.ThemeSettings

class SettingInteractorImpl(private val repository: SettingsRepository) : SettingInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings(App())
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        return repository.updateThemeSetting(settings)
    }
}