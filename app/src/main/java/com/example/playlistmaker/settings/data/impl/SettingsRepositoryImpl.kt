package com.example.playlistmaker.settings.data.impl

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.domain.models.ThemeSettings

class SettingsRepositoryImpl(private val sp:SharedPreferences):SettingsRepository {
    private var darkTheme: Boolean = false

    override fun getThemeSettings(app: Application): ThemeSettings {
        darkTheme = when(app.applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }

        return ThemeSettings(sp.getBoolean(THEME_KEY,darkTheme))
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sp.edit()
            .putBoolean(THEME_KEY, settings.nightTheme)
            .apply()
    }
    companion object{
        private const val THEME_KEY = "key_for_themeSP"
    }
}