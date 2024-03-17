package com.example.playlistmaker

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_menu)

        val shareButton = findViewById<Button>(R.id.Share_but)
        val helpButton = findViewById<Button>(R.id.Help_but)
        val agreeButton = findViewById<Button>(R.id.Agreement_but)
        val backButton = findViewById<Button>(R.id.Set_back_but)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        val adress = getString(R.string.and_dev_go)

        val email = getString(R.string.my_mail)
        val title = getString(R.string.title)
        val text = getString(R.string.text)

        val agree = getString(R.string.agree_adress)


        backButton.setOnClickListener {
            finish()
        }

        val themeSP = getSharedPreferences(THEME_PRETEXT, MODE_PRIVATE)
        themeSwitcher.isChecked = (application as App).darkTheme
        themeSwitcher.setOnCheckedChangeListener{ switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            themeSP.edit()
                .putBoolean(THEME_KEY, checked)
                .apply()
        }

        shareButton.setOnClickListener{
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, adress)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        helpButton.setOnClickListener{

            val mailto = "mailto:$email" +
                    "?subject=" + Uri.encode(title) +
                    "&body=" + Uri.encode(text)

            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse(mailto)

            try {
                startActivity(emailIntent)
            } catch (e: ActivityNotFoundException) {
                //TODO: Handle case where no email app is available
            }
        }

        agreeButton.setOnClickListener{
            val agreeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(agree))
            startActivity(agreeIntent)
        }
    }

    companion object{
        private const val THEME_KEY = "key_for_themeSP"
        private const val THEME_PRETEXT = "key_pretext"
    }
}