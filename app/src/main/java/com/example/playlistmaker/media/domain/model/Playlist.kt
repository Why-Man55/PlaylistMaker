package com.example.playlistmaker.media.domain.model


data class Playlist(val name:String,
    val image:String?,
    val count:Int = 0,
    val info:String?,
    val content:String)
