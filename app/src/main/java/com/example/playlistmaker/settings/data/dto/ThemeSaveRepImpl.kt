package com.example.playlistmaker.settings.data.dto

import android.content.SharedPreferences
import com.example.playlistmaker.settings.domain.api.ThemeSaveRepository

class ThemeSaveRepImpl(private val sp:SharedPreferences): ThemeSaveRepository {
    companion object{
        private const val THEME_KEY = "key_for_themeSP"
    }

    override fun editSP(boolean: Boolean){
        sp.edit()
            .putBoolean(THEME_KEY, boolean)
            .apply()
    }
}