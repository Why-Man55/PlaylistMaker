package com.example.playlistmaker.media.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityNewPlaylistBinding

class NewPlaylistActivity : AppCompatActivity() {
    private var _binding:ActivityNewPlaylistBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.newPlaylistQuiteBut.setOnClickListener{
            finish()
        }

    }
}