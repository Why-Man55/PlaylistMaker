package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.api.TrackOnClicked
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment:Fragment() {
    private var searchText:String? = TEXT_DEF
    private var isClickAllowed = true

    private lateinit var binding: FragmentSearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: HistoryAdapter

    private val viewModel by viewModel<SearchViewModel>()

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModel.delayClick { isClickAllowed = true }
        }
        return current
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchText = savedInstanceState?.getString(SEARCH_TEXT, TEXT_DEF)
        binding.searchBar.setText(searchText)

        val actContext = requireContext()

        val trackOnClicked = object : TrackOnClicked {
            override fun getTrackAndStart(track: Track) {
                if(clickDebounce()){
                    viewModel.saveTrack(track)
                    if(binding.historyMain.isVisible){
                        viewModel.loadHistory()
                    }
                    val displayIntent = Intent(actContext, PlayerActivity::class.java)
                    displayIntent.putExtra("track", Gson().toJson(track))
                    startActivity(displayIntent)
                }
            }
        }

        historyAdapter = HistoryAdapter(trackOnClicked)
        historyAdapter.submitList(listOf())

        trackAdapter = TrackAdapter(trackOnClicked)
        trackAdapter.submitList(listOf())

        viewModel.getSearchRes().observe(this as LifecycleOwner){
            binding.searchLoadingBar.visibility = View.GONE
            trackAdapter.submitList(it.tracks)
            historyAdapter.submitList(it.history)
            if((it.code != -2) and binding.searchBar.text.isNotEmpty()){
                binding.rvTracks.adapter = trackAdapter
                binding.historyMain.visibility = View.GONE
                binding.historyClearBut.visibility = View.GONE
                setVisible(it.code)
                if(it.tracks.isNullOrEmpty() and (it.code != -1)){
                    binding.searchErrorView.visibility = View.VISIBLE
                }
                else{
                    binding.rvTracks.visibility = View.VISIBLE
                }
            }
            else{
                if(it.history.isEmpty()){
                    binding.historyMain.visibility = View.GONE
                    binding.historyClearBut.visibility = View.GONE
                }
                else{
                    binding.historyMain.visibility = View.VISIBLE
                    binding.historyClearBut.visibility = View.VISIBLE
                }
                binding.rvTracks.adapter = historyAdapter
                binding.rvTracks.visibility = View.VISIBLE
            }
        }

        fun searchTrack(){
            viewModel.searchTrack(binding.searchBar.text.toString())
        }
        fun getHistory(){
            binding.searchErrorView.visibility = View.GONE
            binding.internetErrorView.visibility = View.GONE
            viewModel.loadHistory()
        }

        fun searchDebounce() {
            binding.rvTracks.visibility = View.GONE
            binding.historyMain.visibility = View.GONE
            binding.historyClearBut.visibility = View.GONE
            binding.searchErrorView.visibility = View.GONE
            binding.internetErrorView.visibility = View.GONE
            searchTrack()
            binding.searchLoadingBar.visibility = View.VISIBLE
        }

        var runnable = Runnable{searchDebounce()}

        binding.historyClearBut.setOnClickListener {
            viewModel.clearHistory()
            binding.historyMain.visibility = View.GONE
            binding.historyClearBut.visibility = View.GONE
            binding.rvTracks.visibility = View.GONE
            binding.internetErrorView.visibility = View.GONE
            binding.searchErrorView.visibility = View.GONE
        }

        binding.searchBar.setOnFocusChangeListener { view, hasFocus ->
            getHistory()
        }

        binding.rvTracks.layoutManager = LinearLayoutManager(actContext)
        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(binding.searchBar.text.isNotEmpty()){
                    searchDebounce()
                }
                else{
                    getHistory()
                }
                true
            }
            false
        }

        binding.reloadBut.setOnClickListener {
            searchDebounce()
        }

        binding.clearText.setOnClickListener {
            binding.searchBar.setText("")
            getHistory()
            val imm = context?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearText.visibility = clearButtonVisibility(s)
                if(s.isNullOrEmpty()){
                    getHistory()
                }
                else{
                    viewModel.callBackHandler(runnable)
                    runnable = Runnable{searchDebounce()}
                    viewModel.delaySearch(runnable)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }

        }
        binding.searchBar.addTextChangedListener(simpleTextWatcher)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun setVisible(code:Int?){
        when (code) {
            200 -> {
                binding.internetErrorView.visibility = View.GONE
            }
            -1 -> {
                binding.internetErrorView.visibility = View.VISIBLE
                binding.searchErrorView.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val TEXT_DEF = ""
    }
}