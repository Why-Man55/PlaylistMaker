package com.example.playlistmaker.settings.data.dto

import android.app.Application
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.api.ExternalNavigatorRepository

class ExternalNavigatorRepImpl(private val app: Application):ExternalNavigatorRepository {
    override fun returnTextsForSet(): List<String> = listOf(app.getString(R.string.and_dev_go),
        app.getString(R.string.my_mail),
        app.getString(R.string.title),
        app.getString(R.string.text),
        app.getString(R.string.agree_adress))
}