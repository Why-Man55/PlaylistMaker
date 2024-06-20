package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SettingsMenuBinding
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: SettingsMenuBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_menu)

        binding = SettingsMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}