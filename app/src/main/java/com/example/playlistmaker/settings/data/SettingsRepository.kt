package com.example.playlistmaker.settings.data

import android.app.Application
import com.example.playlistmaker.settings.domain.models.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(app: Application): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}