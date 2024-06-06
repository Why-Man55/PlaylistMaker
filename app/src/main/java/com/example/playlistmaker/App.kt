package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitController
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl

class App: Application() {
    var darkTheme:Boolean = false
    override fun onCreate() {
        super.onCreate()

        val themeSP = getSharedPreferences(THEME_PRETEXT, MODE_PRIVATE)
        val themeRep = Creator.getSettingsInt(themeSP)

        switchTheme(themeRep.getThemeSettings().nightTheme)

        Creator.initApplication(this)
    }
    init{
        Creator.initApplication(this)
    }


    fun switchTheme(isDark: Boolean)
    {
        darkTheme = isDark
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    companion object{
        private const val THEME_PRETEXT = "key_pretext"
    }
}
