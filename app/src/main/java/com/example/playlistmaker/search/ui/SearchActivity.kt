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
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.api.TrackOnClicked
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerActivity
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var searchText = TEXT_DEF
    private val inputEditText: EditText by lazy {
        findViewById(R.id.search_bar)
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var binding: ActivitySearchBinding

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DELAY)
        }
        return current
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val baseUrl = getString(R.string.iTunes)

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val iTunes = retrofit.create(ITunesApi::class.java)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val historySP = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)
        val searchHistory = SearchHistory(historySP)

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
            iTunes.search(inputEditText.text.toString()).enqueue(object : Callback<TrackResponse>{
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
                            binding.rvTracks.adapter = TrackAdapter(response.body(), searchHistory, trackOnClicked)
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

        fun showHistory(){
            if (inputEditText.text.isEmpty()){
                if(searchHistory.load().isEmpty()){
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
            binding.rvTracks.adapter = HistoryAdapter(searchHistory.load(), trackOnClicked)
        }

        val searchRunnable = Runnable{searchTrack()}

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DELAY)
            binding.searchLoadingBar.visibility = View.VISIBLE
        }

        binding.historyClearBut.setOnClickListener {
            searchHistory.clearHistory()
            binding.historyMain.visibility = View.GONE
            binding.historyClearBut.visibility = View.GONE
            binding.rvTracks.visibility = View.GONE
            HistoryAdapter(searchHistory.load(), trackOnClicked)
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            showHistory()
        }

        binding.rvTracks.layoutManager = LinearLayoutManager(this)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(inputEditText.text.isNotEmpty()){
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
            inputEditText.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            binding.rvTracks.adapter = HistoryAdapter(searchHistory.load(), trackOnClicked)
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
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, TEXT_DEF)
        inputEditText.setText(searchText)
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

