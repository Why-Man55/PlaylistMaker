package com.example.playlistmaker.settings.presentation

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.data.dto.ExternalNavigatorRepImpl
import com.example.playlistmaker.settings.domain.api.ThemeSaveRepository

class SettingsViewModel(private val themeSaveRepImpl: ThemeSaveRepository):ViewModel() {

    private val strings = ExternalNavigatorRepImpl(Creator.application).returnTextsForSet()
    private var liveData = MutableLiveData(listOf<String>())

    init{
        liveData.postValue(strings)
    }

    fun getTextForSettings():LiveData<List<String>> = liveData

    fun editSP(boolean: Boolean){
        themeSaveRepImpl.editSP(boolean)
    }

    companion object{
        fun getViewModelFactory(sp: SharedPreferences): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(Creator.getThemeSave(sp)) as T
                }
            }
    }
}