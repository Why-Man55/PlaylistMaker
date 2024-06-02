package com.example.playlistmaker.settings.domain.api

interface ExternalNavigatorRepository {
    fun returnTextsForSet():List<String>
}