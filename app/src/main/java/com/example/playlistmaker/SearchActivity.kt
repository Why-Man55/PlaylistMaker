package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val baseUrl = getString(R.string.iTunes)

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val iTunes = retrofit.create(ITunesApi::class.java)

        val clearButton = findViewById<Button>(R.id.clear_text)
        val backButton = findViewById<Button>(R.id.search_back)
        val reloadButton = findViewById<MaterialButton>(R.id.reload_but)
        val historyMas = findViewById<TextView>(R.id.history_main)
        val historyClearBut = findViewById<MaterialButton>(R.id.history_clear_but)

        val searchError = findViewById<LinearLayout>(R.id.search_error_view)
        val internetError = findViewById<LinearLayout>(R.id.internet_error_view)

        val rVTrack = findViewById<RecyclerView>(R.id.rv_tracks)

        val historySP = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)
        val searchHistory = SearchHistory(historySP)


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
                            searchError.visibility = View.VISIBLE
                            rVTrack.visibility = View.GONE
                        }
                        else
                        {
                            rVTrack.visibility = View.VISIBLE
                            internetError.visibility = View.GONE
                            searchError.visibility = View.GONE
                            historyMas.visibility = View.GONE
                            historyClearBut.visibility = View.GONE
                            rVTrack.adapter = TrackAdapter(response.body(), searchHistory)
                        }
                    }
                    else
                    {
                        searchError.visibility = View.VISIBLE
                        rVTrack.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    internetError.visibility = View.VISIBLE
                    rVTrack.visibility = View.GONE
                }
            })
        }

        fun showHistory(){
            if (inputEditText.text.isEmpty()){
                if(searchHistory.load().isEmpty()){
                    historyMas.visibility = View.GONE
                    historyClearBut.visibility = View.GONE
                }
                else{
                    historyMas.visibility = View.VISIBLE
                    historyClearBut.visibility = View.VISIBLE
                    rVTrack.visibility = View.VISIBLE
                }
            }
            else
            {
                historyMas.visibility = View.GONE
                historyClearBut.visibility = View.GONE
                rVTrack.visibility = View.GONE
            }
            rVTrack.adapter = HistoryAdapter(searchHistory.load())
        }

        historyClearBut.setOnClickListener {
            searchHistory.clearHistory()
            historyMas.visibility = View.GONE
            historyClearBut.visibility = View.GONE
            rVTrack.visibility = View.GONE
            HistoryAdapter(searchHistory.load())
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            showHistory()
        }

        rVTrack.layoutManager = LinearLayoutManager(this)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(inputEditText.text.isNotEmpty()){
                    searchTrack()
                }
                true
            }
            false
        }

        reloadButton.setOnClickListener {
            searchTrack()
        }

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            rVTrack.adapter = HistoryAdapter(searchHistory.load())
            internetError.visibility = View.GONE
            searchError.visibility = View.GONE
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                clearButton.visibility = clearButtonVisibility(s)
                showHistory()
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
    }
}