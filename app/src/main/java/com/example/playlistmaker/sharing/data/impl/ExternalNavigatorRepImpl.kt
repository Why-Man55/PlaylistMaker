package com.example.playlistmaker.sharing.data.impl

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.sharing.data.ExternalNavigatorRepository
import com.example.playlistmaker.sharing.domain.model.EmailData
import java.text.SimpleDateFormat
import java.util.Locale

class ExternalNavigatorRepImpl(private val context: Context) : ExternalNavigatorRepository {
    override fun shareLink(text: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(shareIntent)
    }

    override fun openLink(text: String) {
        val agreeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(text))
        agreeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(agreeIntent)
    }

    override fun openEmail(email: EmailData) {
        val mailto = "mailto:${email.myEmail}" +
                "?subject=" + Uri.encode(email.text) +
                "&body=" + Uri.encode(email.title)

        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse(mailto)
        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        try {
            context.startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            //TODO: Handle case where no email app is available
        }
    }

    override fun sharePlaylist(playlist: Playlist, tracks: List<Track>) {
        var text = "${playlist.name}\n" +
                "${playlist.info}\n" +
                "${getTrackCount(playlist.count)}:\n"

        var count = 1
        tracks.map { track ->
            text += "$count. ${track.artistNameItem} - ${track.trackNameItem} (${
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(track.trackTimeItem)
            })\n"
            count++
        }

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(shareIntent)
    }

    private fun getTrackCount(count: Int): String {
        return when {
            count == 11 or 12 or 13 or 14 -> {
                "$count треков"
            }
            count % 10 == 1 -> {
                "$count трек"
            }
            (count % 10 == 2) or (count % 10 == 3) or (count % 10 == 4) -> {
                "$count трека"
            }
            else -> {
                "$count треков"
            }
        }
    }
}