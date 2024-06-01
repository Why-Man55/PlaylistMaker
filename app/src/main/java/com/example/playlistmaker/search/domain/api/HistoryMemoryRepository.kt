package com.example.playlistmaker.search.domain.api

interface HistoryMemoryRepository {
    fun editSP(text: String?)
    fun returnNullJSon(): String?
}