package com.example.playlistmaker.sharing.data

import com.example.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigatorRepository {
    fun shareLink(text:String)
    fun openLink(text:String)
    fun openEmail(email:EmailData)
}