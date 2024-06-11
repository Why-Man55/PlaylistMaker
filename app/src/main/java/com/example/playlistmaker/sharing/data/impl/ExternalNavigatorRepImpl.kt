package com.example.playlistmaker.sharing.data.impl

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.sharing.data.ExternalNavigatorRepository
import com.example.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorRepImpl(private val context: Context): ExternalNavigatorRepository {
    override fun shareLink(text: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    override fun openLink(text: String) {
        val agreeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(text))
        context.startActivity(agreeIntent)
    }

    override fun openEmail(email: EmailData) {
        val mailto = "mailto:${email.myEmail}" +
                "?subject=" + Uri.encode(email.text) +
                "&body=" + Uri.encode(email.title)

        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse(mailto)

        try {
            context.startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            //TODO: Handle case where no email app is available
        }
    }
}