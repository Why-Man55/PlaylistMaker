package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.presentation.PlayerViewModel
import com.example.playlistmaker.search.domain.api.TrackOnClicked
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.impl.HandlerControllerRepimpl
import com.example.playlistmaker.search.domain.impl.SearchHistoryReplmpl
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : ComponentActivity() {

    private var searchText = TEXT_DEF
    private var isClickAllowed = true

    private lateinit var handlerController: HandlerControllerRepimpl
    private lateinit var viewModel : SearchViewModel
    private lateinit var binding: ActivitySearchBinding

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handlerController.postDelay({ isClickAllowed = true },CLICK_DELAY)
        }
        return current
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val historySP = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)
        viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory(historySP))[SearchViewModel::class.java]

        val baseUrl = getString(R.string.iTunes)

        val iTunes = viewModel.createRetrofit(baseUrl).create(ITunesApi::class.java)

        handlerController = HandlerControllerRepimpl()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            iTunes.search(binding.searchBar.text.toString()).enqueue(object : Callback<TrackResponse>{
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.isSuccessful)
                    {
                        if(response.body()?.resultCount == ZERO_COUNT)
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
                            binding.rvTracks.adapter = TrackAdapter(response.body(), viewModel, trackOnClicked)
                        }
                    }
                    else
                    {
                        binding.searchErrorView.visibility = View.VISIBLE
                        binding.rvTracks.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    binding.internetErrorView.visibility = View.VISIBLE
                    binding.rvTracks.visibility = View.GONE
                }
            })
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
            binding.rvTracks.adapter = HistoryAdapter(viewModel.load(), trackOnClicked)
        }

        fun searchDebounce() {
            handlerController.removeCallback(searchRunnable)
            handlerController.postDelay(searchRunnable, SEARCH_DELAY)
            binding.searchLoadingBar.visibility = View.VISIBLE
        }

        binding.historyClearBut.setOnClickListener {
            viewModel.clearHistory()
            binding.historyMain.visibility = View.GONE
            binding.historyClearBut.visibility = View.GONE
            binding.rvTracks.visibility = View.GONE
            HistoryAdapter(viewModel.load(), trackOnClicked)
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
            binding.rvTracks.adapter = HistoryAdapter(viewModel.load(), trackOnClicked)
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
        private const val ZERO_COUNT = 0
        private const val HISTORY_KEY = "key_for_historySP"
        private const val CLICK_DELAY = 1000L
        private const val SEARCH_DELAY = 2000L
    }
}

