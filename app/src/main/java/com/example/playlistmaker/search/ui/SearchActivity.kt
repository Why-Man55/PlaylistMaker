package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.api.TrackOnClicked
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.google.gson.Gson

class SearchActivity : ComponentActivity() {

    private var searchText = TEXT_DEF
    private var isClickAllowed = true

    private lateinit var viewModel : SearchViewModel
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchStates: List<Boolean>

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
        viewModel.searchTrack(binding.searchBar.text.toString())
        viewModel.getStatesSearch().observe(this){
            states -> searchStates = states
        }

        val trackOnClicked = object : TrackOnClicked {
            override fun getTrackAndStart(track: Track) {
                if(clickDebounce()){
                    val displayIntent = Intent(this@SearchActivity, PlayerActivity::class.java)
                    displayIntent.putExtra("track", Gson().toJson(track))
                    startActivity(displayIntent)
                }
            }
        }


        fun searchTrack(){
            viewModel.searchTrack(binding.searchBar.text.toString())
            if(searchStates[2]) {
                binding.internetErrorView.visibility = View.VISIBLE
                binding.rvTracks.visibility = View.GONE
            }
            else {
                if (searchStates[0])
                {
                    if(searchStates[1])
                    {
                        binding.searchErrorView.visibility = View.VISIBLE
                        binding.rvTracks.visibility = View.GONE
                    }
                    else
                    {
                        binding.rvTracks.visibility = View.VISIBLE
                        binding.internetErrorView.visibility = View.GONE
                        binding.searchErrorView.visibility = View.GONE
                        binding.historyMain.visibility = View.GONE
                        binding.historyClearBut.visibility = View.GONE
                        binding.searchLoadingBar.visibility = View.GONE
                        viewModel.getTrackAdapter(trackOnClicked).observe(this){
                            adapter -> binding.rvTracks.adapter = adapter
                        }
                    }
                }
                else
                {
                    binding.searchErrorView.visibility = View.VISIBLE
                    binding.rvTracks.visibility = View.GONE
                }
            }
        }

        val searchRunnable = Runnable{searchTrack()}

        fun showHistory(){
            if (binding.searchBar.text.isEmpty()){
                if(viewModel.load().isEmpty()){
                    binding.historyMain.visibility = View.GONE
                    binding.historyClearBut.visibility = View.GONE
                }
                else{
                    binding.historyMain.visibility = View.VISIBLE
                    binding.historyClearBut.visibility = View.VISIBLE
                    binding.rvTracks.visibility = View.VISIBLE
                }
            }
            else
            {
                binding.historyMain.visibility = View.GONE
                binding.historyClearBut.visibility = View.GONE
                binding.rvTracks.visibility = View.GONE
            }
            viewModel.getHisAdapter(trackOnClicked).observe(this){
                    adapter -> binding.rvTracks.adapter = adapter
            }
        }

        fun searchDebounce() {
            viewModel.callBackHandler(searchRunnable)
            viewModel.delayHandler(searchRunnable)
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
            viewModel.getHisAdapter(trackOnClicked).observe(this){
                    adapter -> binding.rvTracks.adapter = adapter
            }
            binding.internetErrorView.visibility = View.GONE
            binding.searchErrorView.visibility = View.GONE
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
        private const val SEARCH_DELAY = 2000L
    }
}

