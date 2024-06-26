package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.settings.domain.models.ThemeSettings

interface SettingInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}