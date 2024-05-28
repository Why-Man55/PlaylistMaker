package com.example.playlistmaker.settings.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SettingsMenuBinding
import com.example.playlistmaker.settings.domain.impl.App
import com.example.playlistmaker.settings.presentation.SettingsViewModel


class SettingsActivity : ComponentActivity() {

    private lateinit var binding: SettingsMenuBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_menu)

        binding = SettingsMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adress = getString(R.string.and_dev_go)

        val email = getString(R.string.my_mail)
        val title = getString(R.string.title)
        val text = getString(R.string.text)

        val agree = getString(R.string.agree_adress)

        val themeSP = getSharedPreferences(THEME_PRETEXT, MODE_PRIVATE)

        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory(themeSP))[SettingsViewModel::class.java]

        binding.SetBackBut.setOnClickListener {
            finish()
        }

        binding.themeSwitcher.isChecked = (application as App).darkTheme
        binding.themeSwitcher.setOnCheckedChangeListener{ switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            viewModel.editSP(checked)
        }

        binding.ShareBut.setOnClickListener{
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, adress)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.HelpBut.setOnClickListener{

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

        binding.AgreementBut.setOnClickListener{
            val agreeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(agree))
            startActivity(agreeIntent)
        }
    }
    companion object{
        private const val THEME_PRETEXT = "key_pretext"
    }
}