package com.example.playlistmaker.search.domain.models

data class ResponseStates(
    val isSuccessful:Boolean,
    val zeroCount:Boolean,
    val internetError:Boolean
)