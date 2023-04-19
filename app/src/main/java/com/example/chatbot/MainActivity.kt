package com.example.chatbot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputEditText: TextInputEditText
    private lateinit var mAdapter: BotMessageAdapter
    private lateinit var messageList: ArrayList<ChatModel>
    val apiKey = "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"
    private var url = "https://api.openai.com/v1/completions"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.chat_rv)
        inputEditText = findViewById(R.id.et_text_message)
        messageList = ArrayList()
        mAdapter = BotMessageAdapter(messageList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
        inputEditText.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEND) {
                if (inputEditText.toString().length > 0) {
                    messageList.add(ChatModel(inputEditText.text.toString(), "user"))
                    mAdapter.notifyDataSetChanged()
                    getResponse()
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun getResponse(query: String) {
        inputEditText.setText("")
        val queue = Volley.newRequestQueue(this)
        val jsonObject = JSONObject()
        jsonObject.put("model", "text-davinci-003")
        jsonObject.put("prompt", query)
        jsonObject.put("temperature", 0)
        jsonObject.put("max_tokens", 100)
        jsonObject.put("top_p", 1)
        jsonObject.put("frequency_penalty", 0.0)
        jsonObject.put("presence_penalty", 0.0)

        val postRequest = JsonObjectRequest(Request.Method.POST,url,jsonObject,Response.Listener { response ->
            val respMsg = response.getJSONArray("choices").getJSONObject(0).getString("text")
            messageList.add(ChatModel(respMsg,"bot"))
        },{

        })


    }
}
