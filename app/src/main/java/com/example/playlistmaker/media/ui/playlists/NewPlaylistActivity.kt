package com.example.playlistmaker.media.ui.playlists

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityNewPlaylistBinding
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.presentation.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

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
                    imageUri = uri
                    binding.newPlaylistImage.setImageURI(imageUri)
                    saveImage(imageUri!!)
                    isChanged = true
                }
            }

        val quiteDialoge = MaterialAlertDialogBuilder(this)
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

        binding.newPlaylistImage.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createPlaylistBut.setOnClickListener{
            val newName = binding.newPlaylistNameEt.text.toString()
            viewModel.updatePlaylists(Playlist(newName, imageUri.toString(), 0, binding.newPlaylistInfEt.text.toString(), "", null))
            Toast.makeText(this, "Плейлист $newName создан", Toast.LENGTH_LONG).show()
            finish()
        }

        val nameWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //empty
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val empty = !binding.newPlaylistNameEt.text.isNullOrEmpty()
                if(empty){
                    binding.createPlaylistBut.setBackgroundResource(R.color.back_blue)
                }
                else{
                    binding.createPlaylistBut.setBackgroundResource(R.color.yp_gray)
                }
                binding.createPlaylistBut.isEnabled = empty
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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

    private fun saveImage(uri: Uri) {
        val filePath = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val file = File(filePath, binding.newPlaylistNameEt.text.toString() + "_playlist_image.jpg")
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }
}