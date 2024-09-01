package com.example.playlistmaker.sharing.domain.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.ExternalNavigatorRepository
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData

class SharingIntractorImpl(
    private val navigator: ExternalNavigatorRepository,
    private val app: Context
) : SharingInteractor {

    override fun shareApp() {
        navigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        navigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        navigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return app.getString(R.string.and_dev_go)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            app.getString(R.string.my_mail),
            app.getString(R.string.title),
            app.getString(R.string.text)
        )
    }

    private fun getTermsLink(): String {
        return app.getString(R.string.agree_adress)
    }
}