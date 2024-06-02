package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

class App: Application() {
    var darkTheme: Boolean = false



    override fun onCreate() {
        super.onCreate()

        darkTheme = when(applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }

        val themeSP = getSharedPreferences(THEME_PRETEXT, MODE_PRIVATE)
        val gotThemeSP = themeSP.getBoolean(THEME_KEY, darkTheme)
        switchTheme(gotThemeSP)

        Creator.setApplication(this)
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
        private const val THEME_KEY = "key_for_themeSP"
    }
}
