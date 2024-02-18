package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
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

        val translateBaseUrl = getString(R.string.iTunes)

        val retrofit = Retrofit.Builder()
            .baseUrl(translateBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val iTunes = retrofit.create(ITunesApi::class.java)

        var tracks: List<TrackResponse>? = listOf()

        val clearButton = findViewById<Button>(R.id.clear_text)
        val backButton = findViewById<Button>(R.id.search_back)
        val reloadButton = findViewById<MaterialButton>(R.id.reload_but)

        val searchError = findViewById<LinearLayout>(R.id.search_error_view)
        val internetError = findViewById<LinearLayout>(R.id.internet_error_view)

        val rVTrack = findViewById<RecyclerView>(R.id.rv_tracks)
        rVTrack.layoutManager = LinearLayoutManager(this)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                iTunes.search(inputEditText.text.toString()).enqueue(object : Callback<List<TrackResponse>>{
                    override fun onResponse(
                        call: Call<List<TrackResponse>>,
                        response: Response<List<TrackResponse>>
                    ) {
                        if (response.code() != 200)
                        {
                            searchError.visibility = View.VISIBLE
                            rVTrack.visibility = View.GONE
                        }
                        else
                        {
                            tracks = response.body()
                        }
                    }

                    override fun onFailure(call: Call<List<TrackResponse>>, t: Throwable) {
                        internetError.visibility = View.VISIBLE
                        rVTrack.visibility = View.GONE
                    }
                })

                rVTrack.adapter = TrackAdapter(tracks!!.toList())
                true
            }
            false
        }

        reloadButton.setOnClickListener {
            iTunes.search(inputEditText.text.toString()).enqueue(object : Callback<List<TrackResponse>>{
                override fun onResponse(
                    call: Call<List<TrackResponse>>,
                    response: Response<List<TrackResponse>>
                ) {
                    if (response.code() != 200)
                    {
                        searchError.visibility = View.VISIBLE
                        rVTrack.visibility = View.GONE
                    }
                    else
                    {
                        tracks = response.body()
                    }
                }

                override fun onFailure(call: Call<List<TrackResponse>>, t: Throwable) {
                    internetError.visibility = View.VISIBLE
                    rVTrack.visibility = View.GONE
                }
            })

            rVTrack.adapter = TrackAdapter(tracks!!.toList())
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