package com.example.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

data class TrackDto(
    @SerializedName("trackName") val trackNameItem: String,
    @SerializedName("artistName") val artistNameItem: String,
    @SerializedName("trackTimeMillis") val trackTimeItem: Int,
    @SerializedName("artworkUrl100") val trackAvatarItem: String,
    @SerializedName("trackId") val trackID: Int,
    @SerializedName("collectionName") val collectionName: String,
    @SerializedName("releaseDate") val rYear: String,
    @SerializedName("primaryGenreName") val genre: String,
    @SerializedName("country") val country: String,
    @SerializedName("previewUrl") val audioUrl: String
)