package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.media.di.mediaViewModule
import com.example.playlistmaker.player.di.playerInteractorModule
import com.example.playlistmaker.player.di.playerRepositoryModule
import com.example.playlistmaker.player.di.playerViewModelModule
import com.example.playlistmaker.search.di.searchDataModule
import com.example.playlistmaker.search.di.searchInteractorModule
import com.example.playlistmaker.search.di.searchRepositoryModule
import com.example.playlistmaker.search.di.searchViewModelModule
import com.example.playlistmaker.settings.di.settingsRepositoryModule
import com.example.playlistmaker.settings.di.settingsViewModelModule
import com.example.playlistmaker.settings.di.settingsaIntModule
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.sharing.di.sharingIntModule
import com.example.playlistmaker.sharing.di.sharingsRepModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    var darkTheme:Boolean = false
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(searchDataModule, searchInteractorModule, searchRepositoryModule, searchViewModelModule,playerInteractorModule, playerRepositoryModule, playerViewModelModule,
                settingsaIntModule, settingsViewModelModule, settingsRepositoryModule,sharingsRepModule, sharingIntModule,mediaViewModule)
        }

        val themeSP = getSharedPreferences(THEME_PRETEXT, MODE_PRIVATE)
        val themeRep = SettingsRepositoryImpl(themeSP)

        switchTheme(themeRep.getThemeSettings(this).nightTheme)
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
