package com.example.playlistmaker.search.domain.models

data class Track(
    val trackNameItem:String,
    val artistNameItem:String,
    val trackTimeItem:Int,
    val trackAvatarItem:String,
    val trackID: Int,
    val collectionName:String,
    val rYear:String?,
    val genre:String,
    val country:String,
    val audioUrl:String?)