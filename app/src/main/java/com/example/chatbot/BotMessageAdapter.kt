package com.example.chatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import java.security.MessageDigest

class BotMessageAdapter(private val messageList:List<ChatModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class UserViewHolder(item:View):RecyclerView.ViewHolder(item){
        val userTv:TextView = item.findViewById(R.id.tv_user_message)
    }
    class BotViewHolder(item:View):RecyclerView.ViewHolder(item){
        val botTv:TextView = item.findViewById(R.id.tv_bot_message)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View
        return if (viewType == 0){
            view = LayoutInflater.from(parent.context).inflate(R.layout.bot_chat_layout,parent,false)
            BotViewHolder(view)
        }else{
            view = LayoutInflater.from(parent.context).inflate(R.layout.user_chat_item_layou,parent,false)
            UserViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
         return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(messageList[position].sender){
            "user"-> (holder as UserViewHolder).userTv.text = messageList[position].message
            "bot"-> (holder as BotViewHolder).botTv.text = messageList[position].message
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(messageList[position].sender){
            "user" -> 1
            "bot" -> 0
            else -> 0
        }
    }


}