package com.example.playlistmaker.media.presentation.objects

import android.content.Context
import java.io.InputStream
import java.util.Date

data class NPVMInputObject(
    val context: Context,
    val name: String,
    val inputStream: InputStream?,
    val time: Date
)
