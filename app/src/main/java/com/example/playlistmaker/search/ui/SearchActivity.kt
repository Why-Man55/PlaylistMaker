package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.api.TrackOnClicked
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    private var searchText = TEXT_DEF
    private var isClickAllowed = true

    private lateinit var viewModel : SearchViewModel
    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: HistoryAdapter

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModel.delayClick { isClickAllowed = true }
        }
        return current
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val historySP = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)
        viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory(historySP, this))[SearchViewModel::class.java]

        val trackOnClicked = object : TrackOnClicked {
            override fun getTrackAndStart(track: Track) {
                if(clickDebounce()){
                    val displayIntent = Intent(this@SearchActivity, PlayerActivity::class.java)
                    displayIntent.putExtra("track", Gson().toJson(track))
                    startActivity(displayIntent)
                }
            }
        }

        historyAdapter = HistoryAdapter(trackOnClicked)
        historyAdapter.submitList(listOf())

        trackAdapter = TrackAdapter(this, historySP, trackOnClicked)
        trackAdapter.submitList(listOf())

        viewModel.getSearchRes().observe(this){
            trackAdapter.submitList(it.tracks)
            binding.rvTracks.adapter = trackAdapter
            setVisible(it.code)
            if(it.tracks.isNullOrEmpty()){
                binding.searchErrorView.visibility = View.VISIBLE
            }
            binding.rvTracks.visibility = View.VISIBLE
            binding.searchLoadingBar.visibility = View.GONE
            binding.historyMain.visibility = View.GONE
            binding.historyClearBut.visibility = View.GONE
        }

        viewModel.getHistory().observe(this){
            historyAdapter.submitList(it)
            binding.rvTracks.adapter = historyAdapter
            if(it.isEmpty()){
                binding.historyMain.visibility = View.GONE
                binding.historyClearBut.visibility = View.GONE
            }
            else{
                binding.historyMain.visibility = View.VISIBLE
                binding.historyClearBut.visibility = View.VISIBLE
                binding.rvTracks.visibility = View.VISIBLE
            }
        }


        fun searchTrack(){
            viewModel.searchTrack(binding.searchBar.text.toString())
        }

        fun showHistory(){
            viewModel.getHistoryList()
            binding.searchErrorView.visibility = View.GONE
            binding.searchLoadingBar.visibility = View.GONE
        }

        fun searchDebounce() {
            if(binding.searchBar.text.isNotEmpty()){
                val runnable = Runnable{searchTrack()}
                viewModel.callBackHandler(runnable)
                viewModel.delaySearch(runnable)
                binding.searchLoadingBar.visibility = View.VISIBLE
            }
            else{
                showHistory()
            }
        }

        binding.historyClearBut.setOnClickListener {
            viewModel.clearHistory()
            binding.historyMain.visibility = View.GONE
            binding.historyClearBut.visibility = View.GONE
            binding.rvTracks.visibility = View.GONE
            binding.internetErrorView.visibility = View.GONE
            binding.searchErrorView.visibility = View.GONE
        }

        binding.searchBar.setOnFocusChangeListener { view, hasFocus ->
            showHistory()
        }

        binding.rvTracks.layoutManager = LinearLayoutManager(this)
        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(binding.searchBar.text.isNotEmpty()){
                    searchTrack()
                }
                true
            }
            false
        }

        binding.reloadBut.setOnClickListener {
            searchTrack()
        }

        binding.searchBack.setOnClickListener {
            finish()
        }

        binding.clearText.setOnClickListener {
            binding.searchBar.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearText.visibility = clearButtonVisibility(s)
                binding.rvTracks.visibility = View.GONE
                binding.historyMain.visibility = View.GONE
                binding.historyClearBut.visibility = View.GONE
                binding.searchErrorView.visibility = View.GONE
                binding.internetErrorView.visibility = View.GONE
                searchDebounce()
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, TEXT_DEF)
        binding.searchBar.setText(searchText)
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
        private const val HISTORY_KEY = "key_for_historySP"
    }
}

