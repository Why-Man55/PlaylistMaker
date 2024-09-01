package com.example.playlistmaker.player.presentation

import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track

data class PlayerVMObjects(val time:Long,val track: Track,val playlists:List<Playlist>,val isLoved:Boolean)