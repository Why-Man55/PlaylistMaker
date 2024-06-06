package com.example.playlistmaker.settings.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SettingsMenuBinding
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.presentation.SettingsViewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: SettingsMenuBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_menu)

        binding = SettingsMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sp = getSharedPreferences(THEME_PRETEXT, MODE_PRIVATE)

        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory(this,sp))[SettingsViewModel::class.java]

        binding.SetBackBut.setOnClickListener {
            finish()
        }

        binding.themeSwitcher.isChecked = (application as App).darkTheme
        binding.themeSwitcher.setOnCheckedChangeListener{ switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            viewModel.editSP(checked)
        }

        binding.ShareBut.setOnClickListener{
            viewModel.startShare()
        }

        binding.HelpBut.setOnClickListener{
            viewModel.startSupport()
        }

        binding.AgreementBut.setOnClickListener{
            viewModel.startAgreement()
        }
    }
    companion object{
        private const val THEME_PRETEXT = "key_pretext"
    }
}