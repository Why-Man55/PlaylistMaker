package com.example.playlistmaker.settings.domain.impl

import android.content.SharedPreferences
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingInteractor
import com.example.playlistmaker.settings.domain.models.ThemeSettings

class SettingInteractorImpl(sp:SharedPreferences):SettingInteractor{
    private val repository:SettingsRepository = SettingsRepositoryImpl(sp)
        override fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        return repository.updateThemeSetting(settings)
    }
}