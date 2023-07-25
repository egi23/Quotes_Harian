package com.example.katakatahariini

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.katakatahariini.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val QUOTE_URL = "https://api.quotable.io/random"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getRandomQuote()
        binding.btnAllQuotes.setOnClickListener {
            startActivity(Intent(this@MainActivity, ListQuotesActivity::class.java))
        }
    }

    private fun getRandomQuote() {
        binding.progressBar.visibility = ProgressBar.VISIBLE
        val client = AsyncHttpClient()
        client.get(QUOTE_URL, object : JsonHttpResponseHandler() {
            @SuppressLint("StringFormatInvalid")
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {
                binding.progressBar.visibility = ProgressBar.GONE
                try {
                    val content = response?.getString("content")
                    val author = response?.getString("author")
                    binding.tvQuote.text = content
                    binding.tvAuthor.text = getString(R.string.author, author)
                } catch (e: JSONException) {
                    Log.e(TAG, "Failed to parse JSON response", e)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                throwable: Throwable?,
                errorResponse: JSONObject?
            ) {
                binding.progressBar.visibility = ProgressBar.GONE
                Log.e(TAG, "Request to $QUOTE_URL failed. Status code: $statusCode", throwable)
            }
        })
    }
}