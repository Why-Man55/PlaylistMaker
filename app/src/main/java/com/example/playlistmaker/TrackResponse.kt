package com.example.playlistmaker

import com.google.gson.annotations.SerializedName

class TrackResponse (
    @SerializedName("trackName")val trackNameItem:String,
    @SerializedName("artistName")val artistNameItem:String,
    @SerializedName("trackTimeMillis")val trackTimeItem:Int,
    @SerializedName("artworkUrl100")val trackAvatarItem:String
)
