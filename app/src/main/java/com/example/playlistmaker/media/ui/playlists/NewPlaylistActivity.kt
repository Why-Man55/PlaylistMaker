package com.example.playlistmaker.media.ui.playlists

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityNewPlaylistBinding
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.presentation.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar
import java.util.Date

class NewPlaylistActivity : AppCompatActivity() {
    private val viewModel by viewModel<NewPlaylistViewModel>()
    private var _binding:ActivityNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private var imageUri:Uri? = null
    private var isChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(this).load(uri).centerCrop().transform(CenterCrop(),RoundedCorners(16)).into(binding.newPlaylistImage)
                    imageUri = uri
                    isChanged = true
                }
            }

        val quiteDialoge = MaterialAlertDialogBuilder(this, R.style.dialog)
            .setTitle(getString(R.string.new_playlist_quite_title))
            .setMessage(getString(R.string.new_playlist_quite_massage))
            .setNeutralButton(getString(R.string.new_playlist_resume)) { _, _ ->
                // empty
            }
            .setPositiveButton(getString(R.string.new_playlst_cancel)) { _, _ ->
                finish()
            }

        binding.newPlaylistQuiteBut.setOnClickListener{
            if(isChanged or binding.newPlaylistInfEt.text.isNotEmpty() or binding.newPlaylistNameEt.text.isNotEmpty()){
                quiteDialoge.show()
            }
            else{
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                quiteDialoge.show()
            }
        })

        binding.newPlaylistImage.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createPlaylistBut.setOnClickListener{
            val newName = binding.newPlaylistNameEt.text.toString()
            val currentTime: Date = Calendar.getInstance().time
            var savedImageUri = imageUri.toString()
            if (imageUri.toString().isNotEmpty()){
                savedImageUri = saveImage(imageUri!!,currentTime, newName).toString()
            }
            viewModel.updatePlaylists(Playlist(newName, savedImageUri, 0, binding.newPlaylistInfEt.text.toString(), "", null))
            Toast.makeText(this, "Плейлист $newName создан", Toast.LENGTH_LONG).show()
            finish()
        }

        val nameWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //empty
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val empty = binding.newPlaylistNameEt.text.isEmpty()
                if(empty){
                    binding.createPlaylistBut.setBackgroundResource(R.drawable.gray_button)
                }
                else{
                    binding.createPlaylistBut.setBackgroundResource(R.drawable.blue_button)
                }
                binding.createPlaylistBut.isEnabled = !empty
            }

            override fun afterTextChanged(p0: Editable?) {
                //empty
            }
        }
        binding.newPlaylistNameEt.addTextChangedListener(nameWatcher)

        binding.newPlaylistNameEt.setOnFocusChangeListener { _, b ->
            bindET(b, binding.newPlaylistNameEt, binding.newNameTitle)
        }

        binding.newPlaylistInfEt.setOnFocusChangeListener { _, b ->
            bindET(b, binding.newPlaylistInfEt, binding.newInfoTitle)
        }
    }

    private fun bindET(b:Boolean, view: EditText, text: TextView){
        val boolean = b or view.text.isNotEmpty()
        text.isVisible = boolean
        if(boolean){
            view.setBackgroundResource(R.drawable.new_playlist_et_focus_back)
        }
        else{
            view.setBackgroundResource(R.drawable.new_playlist_et_back)
        }
    }

    private fun saveImage(uri: Uri, time:Date, name:String) : Uri {
        return viewModel.saveImage(this, name, contentResolver.openInputStream(uri), time)
    }
}