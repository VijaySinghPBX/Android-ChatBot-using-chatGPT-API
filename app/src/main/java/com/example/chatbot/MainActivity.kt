package com.example.chatbot

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputEditText: TextInputEditText
    private lateinit var mAdapter: BotMessageAdapter
    private lateinit var messageList: ArrayList<ChatModel>
    private var url = "https://api.openai.com/v1/completions"
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.chat_rv)
        inputEditText = findViewById(R.id.et_text_message)
        messageList = ArrayList()
        mAdapter = BotMessageAdapter(messageList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
        inputEditText.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEND){
                var message = inputEditText.text.toString()
                if (message.isNotBlank()){
                    messageList.add(ChatModel(message,"user"))
                    mAdapter.notifyDataSetChanged()
                    getResponse(message)
                    inputEditText.setText("")
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun getResponse(query: String) {
        inputEditText.setText("")
        val queue = Volley.newRequestQueue(this)
        val jsonObject: JSONObject = JSONObject()
        jsonObject.put("model", "text-davinci-003")
        jsonObject.put("prompt", query)
        jsonObject.put("temperature", 0)
        jsonObject.put("max_tokens", 100)
        jsonObject.put("top_p", 1)
        jsonObject.put("frequency_penalty", 0.0)
        jsonObject.put("presence_penalty", 0.0)
        val postRequest = object: JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonObject,
            { respone ->
                val messageRes = respone.getJSONArray("choices").getJSONObject(0).getString("text")
                messageList.add(ChatModel(messageRes, "bot"))
                mAdapter.notifyDataSetChanged()
            },
            {
                Log.e("error", "some error occurred")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer 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"
                return params
            }
        }
        queue.add(postRequest)
    }

}
