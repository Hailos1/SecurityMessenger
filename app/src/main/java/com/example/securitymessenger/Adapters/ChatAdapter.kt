package com.example.securitymessenger.Adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.securitymessenger.ChatActivity
import com.example.securitymessenger.MainActivity
import com.example.securitymessenger.Model.Chat
import com.example.securitymessenger.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(mContext: Context, mChats: List<Chat>, isChatCheck: Boolean, jwt: String, userId: String) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder?>() {

    private val mContext: Context
    private val mChats: List<Chat>
    private var isChatCheck: Boolean
    private var jwt: String
    private var userId: String

    init {
        this.mContext = mContext
        this.mChats = mChats
        this.isChatCheck = isChatCheck
        this.jwt = jwt
        this.userId = userId
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ChatId: TextView
        var ChatName: TextView
        var icon: CircleImageView

        init {
            ChatId = itemView.findViewById(R.id.chat_id)
            ChatName = itemView.findViewById(R.id.user_name_chat)
            icon = itemView.findViewById(R.id.profile_image_chat)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int):ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.chat_item_layout, viewGroup, false)
        return ChatAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mChats.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat: Chat = mChats[position]
        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, ChatActivity::class.java)
            intent.putExtra("jwt", jwt);
            intent.putExtra("chatid", chat.chatId.toString())
            intent.putExtra("userId", userId)
            println(chat.chatId.toString())
            startActivity(mContext, intent, Bundle.EMPTY)
        }
        holder.ChatId.text = chat.chatId.toString()
        holder.ChatName.text = chat.chatName.toString()
        Picasso.get().load(chat.chatImg).placeholder(R.drawable.ic_profile).into(holder.icon)
    }
}