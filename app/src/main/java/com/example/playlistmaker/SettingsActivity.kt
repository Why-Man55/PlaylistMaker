package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_menu)

        val shareButton = findViewById<Button>(R.id.Share_but)
        val helpButton = findViewById<Button>(R.id.Help_but)
        val agreeButton = findViewById<Button>(R.id.Agreement_but)

        shareButton.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/profile/android-developer/")
            startActivity(shareIntent)
        }

        helpButton.setOnClickListener{
            val helpIntent = Intent(Intent.ACTION_SENDTO)
            helpIntent.putExtra(Intent.EXTRA_EMAIL, "Galimivkamil452@gmail.com")
            helpIntent.putExtra(Intent.EXTRA_SUBJECT, "Сообщение разработчикам и разработчицам приложения Playlist Maker")
            helpIntent.putExtra(Intent.EXTRA_TEXT, "Спасибо разработчикам и разработчицам за крутое приложение!")
            startActivity(helpIntent)
        }

        agreeButton.setOnClickListener{
            val agreeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
            startActivity(agreeIntent)
        }
    }
}