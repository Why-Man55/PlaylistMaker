package com.example.playlistmaker.media.ui.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistsFragmentBinding
import com.example.playlistmaker.media.presentation.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    companion object {
        fun newInstance(): Fragment = PlaylistFragment().apply {}
    }

    private val viewModel by viewModel<PlaylistsFragmentViewModel>()
    private var _binding: PlaylistsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PlaylistFragmentAdapter()
        adapter.submitList(listOf())
        viewModel.returnPlaylists().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.playlistList.layoutManager = GridLayoutManager(requireActivity(), 2)
            binding.playlistList.adapter = adapter
            if (it.isEmpty()) {
                setError(true)
            } else {
                setError(false)
            }
        }

        binding.newPlaylistBut.setOnClickListener {
            findNavController().navigate(R.id.action_mediatekFragment_to_newPlaylistActivity)
        }
        viewModel.getPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setError(boolean: Boolean) {
        binding.playlistErrorMassage.isVisible = boolean
    }
}