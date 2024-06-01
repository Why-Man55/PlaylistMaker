package com.example.playlistmaker.settings.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SettingsMenuBinding
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.presentation.SettingsViewModel


class SettingsActivity : ComponentActivity() {

    private lateinit var binding: SettingsMenuBinding
    private lateinit var viewModel: SettingsViewModel

    private lateinit var texts: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_menu)

        binding = SettingsMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sp = getSharedPreferences(THEME_PRETEXT, MODE_PRIVATE)

        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory(sp))[SettingsViewModel::class.java]
        viewModel.getTextForSettings().observe(this){
            textsForSettings -> texts = textsForSettings
        }

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
                putExtra(Intent.EXTRA_TEXT, texts[0])
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.HelpBut.setOnClickListener{

            val mailto = "mailto:${texts[1]}" +
                    "?subject=" + Uri.encode(texts[2]) +
                    "&body=" + Uri.encode(texts[3])

            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse(mailto)

            try {
                startActivity(emailIntent)
            } catch (e: ActivityNotFoundException) {
                //TODO: Handle case where no email app is available
            }
        }

        binding.AgreementBut.setOnClickListener{
            val agreeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(texts[4]))
            startActivity(agreeIntent)
        }
    }
    companion object{
        private const val THEME_PRETEXT = "key_pretext"
    }
}