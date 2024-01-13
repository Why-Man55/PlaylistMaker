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
        val backButton = findViewById<Button>(R.id.Set_back_but)

        val adress = getString(R.string.and_dev_go)

        val email = getString(R.string.my_mail)
        val title = getString(R.string.title)
        val text = getString(R.string.text)

        val agree = getString(R.string.agree_adress)

        backButton.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }

        shareButton.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, adress)
            startActivity(shareIntent)
        }

        helpButton.setOnClickListener{
            val helpIntent = Intent(Intent.ACTION_SENDTO)
            helpIntent.putExtra(Intent.EXTRA_EMAIL, email)
            helpIntent.putExtra(Intent.EXTRA_SUBJECT, title)
            helpIntent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(helpIntent)
        }

        agreeButton.setOnClickListener{
            val agreeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(agree))
            startActivity(agreeIntent)
        }
    }
}