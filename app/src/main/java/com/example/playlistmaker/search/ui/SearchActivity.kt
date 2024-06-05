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
import com.example.playlistmaker.search.domain.models.ResponseStates
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    private var searchText = TEXT_DEF
    private var isClickAllowed = true

    private lateinit var viewModel : SearchViewModel
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchStates: ResponseStates
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: HistoryAdapter

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModel.delayHandler { isClickAllowed = true }
        }
        return current
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val historySP = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)
        viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory(historySP))[SearchViewModel::class.java]

        val trackOnClicked = object : TrackOnClicked {
            override fun getTrackAndStart(track: Track) {
                if(clickDebounce()){
                    val displayIntent = Intent(this@SearchActivity, PlayerActivity::class.java)
                    displayIntent.putExtra("track", Gson().toJson(track))
                    startActivity(displayIntent)
                }
            }
        }

        historyAdapter = HistoryAdapter(listOf(),trackOnClicked)

        viewModel.getSearchRep().observe(this){
            rep -> trackAdapter = TrackAdapter(rep, trackOnClicked)
            trackAdapter.submitList(listOf())
        }
        viewModel.getStatesSearch().observe(this){
            searchStates = it.responseStates
            binding.searchBar.setText(it.response?.resultCount.toString())
            trackAdapter.submitList(it.response?.results)

            if(searchStates.internetError) {
                binding.rvTracks.visibility = View.GONE
                binding.internetErrorView.visibility = View.VISIBLE
            }
            else {
                if (searchStates.isSuccessful)
                {

                    if(searchStates.zeroCount)
                    {
                        binding.searchErrorView.visibility = View.VISIBLE
                    }
                    else
                    {
                        binding.rvTracks.adapter = trackAdapter
                        binding.rvTracks.visibility = View.VISIBLE
                        binding.internetErrorView.visibility = View.GONE
                        binding.searchErrorView.visibility = View.GONE
                        binding.historyMain.visibility = View.GONE
                        binding.historyClearBut.visibility = View.GONE
                        binding.searchLoadingBar.visibility = View.GONE
                    }
                }
                else
                {
                    binding.rvTracks.visibility = View.GONE
                    binding.searchLoadingBar.visibility = View.GONE
                }
            }
        }
        viewModel.getHistory().observe(this){
                his ->
            if(his.isEmpty()){
                binding.historyMain.visibility = View.GONE
                binding.historyClearBut.visibility = View.GONE
            }
            else{
                binding.historyMain.visibility = View.VISIBLE
                binding.historyClearBut.visibility = View.VISIBLE
                binding.rvTracks.visibility = View.VISIBLE
            }
            historyAdapter.submitList(his)
            binding.rvTracks.adapter = historyAdapter
        }


        fun searchTrack(){
            viewModel.searchTrack(binding.searchBar.text.toString())
        }

        fun showHistory(){
            if (binding.searchBar.text.isEmpty()){
                viewModel.load()
                binding.searchErrorView.visibility = View.GONE
                binding.searchLoadingBar.visibility = View.GONE
            }
            else
            {
                binding.historyMain.visibility = View.GONE
                binding.historyClearBut.visibility = View.GONE
                binding.rvTracks.visibility = View.GONE
            }
        }

        fun searchDebounce() {
            val runnable = Runnable{searchTrack()}
            viewModel.callBackHandler(runnable)
            viewModel.delayHandler(runnable)
            binding.searchLoadingBar.visibility = View.VISIBLE
        }

        binding.historyClearBut.setOnClickListener {
            viewModel.clearHistory()
            binding.historyMain.visibility = View.GONE
            binding.historyClearBut.visibility = View.GONE
            binding.rvTracks.visibility = View.GONE
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
                showHistory()
                if(s.toString().isNotEmpty())
                {
                    searchDebounce()
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

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val TEXT_DEF = ""
        private const val HISTORY_KEY = "key_for_historySP"
    }
}

