package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    private var searchText = TEXT_DEF

    private val inputEditText: EditText by lazy {
        findViewById(R.id.search_bar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val tracks:List<Track> = listOf(Track(getString(R.string.track1_name), getString(R.string.track1_artist), getString(R.string.track1_time), getString(R.string.track1_url)),
            Track(getString(R.string.track2_name), getString(R.string.track2_artist), getString(R.string.track2_time), getString(R.string.track2_url)),
            Track(getString(R.string.track3_name), getString(R.string.track3_artist), getString(R.string.track3_time), getString(R.string.track3_url)),
            Track(getString(R.string.track4_name), getString(R.string.track4_artist), getString(R.string.track4_time), getString(R.string.track4_url)),
            Track(getString(R.string.track5_name), getString(R.string.track5_artist), getString(R.string.track5_time), getString(R.string.track5_url)))

        val clearButton = findViewById<Button>(R.id.clear_text)
        val backButton = findViewById<Button>(R.id.search_back)

        val rVTrack = findViewById<RecyclerView>(R.id.rv_tracks)
        rVTrack.layoutManager = LinearLayoutManager(this)
        val trAdapter = TrackAdapter(tracks)
        rVTrack.adapter = trAdapter

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
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                clearButton.visibility = clearButtonVisibility(s)
                searchText = inputEditText.text.toString()
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
    }
}