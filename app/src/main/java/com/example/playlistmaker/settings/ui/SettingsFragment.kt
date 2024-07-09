package com.example.playlistmaker.settings.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment:Fragment() {
    companion object{
        fun newInstance() = SettingsFragment().apply {  }
    }
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.themeSwitcher.isChecked = (activity?.applicationContext as App).darkTheme
        binding.themeSwitcher.setOnCheckedChangeListener{ switcher, checked ->
            (activity?.applicationContext as App).switchTheme(checked)
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