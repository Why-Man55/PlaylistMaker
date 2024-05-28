package com.example.playlistmaker.settings.data.dto

import android.content.SharedPreferences

class ThemeSave(private val sp:SharedPreferences) {
    companion object{
        private const val THEME_KEY = "key_for_themeSP"
    }

    fun editSP(boolean: Boolean){
        sp.edit()
            .putBoolean(THEME_KEY, boolean)
            .apply()
    }
}