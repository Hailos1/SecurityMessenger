package com.example.securitymessenger

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.securitychat.signalR.SignalRListener
import com.example.securitymessenger.Adapters.MessageAdapter
import com.example.securitymessenger.Model.Message
import com.example.securitymessenger.RestClientApi.MessageApi
import com.example.securitymessenger.Services.NotificationService
import com.example.securitymessenger.databinding.ActivityChatBinding
import com.google.gson.JsonElement
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    public lateinit var jwt: String
    private lateinit var userId: String
    private var chatid: Int? = null
    private var chatImg: String? = null
    private var chatName: String? = null

    private var MessageAdapter: MessageAdapter? = null
    private var mMessages: ArrayList<com.example.securitymessenger.Model.Message>? = null
    private var recyclerView: RecyclerView? = null
    private var newMessage: EditText? = null
    private var sendButton: ImageView? = null

    private lateinit var signalRListener: SignalRListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = intent.extras
        if (arguments != null) {
            jwt = arguments.get("jwt").toString()
            userId = arguments.get("userId").toString()
            chatid = arguments.get("chatid").toString().toInt()
            chatImg = arguments.get("chatImg").toString()
            chatName = arguments.get("chatName").toString()
        }

        binding = ActivityChatBinding.inflate(layoutInflater)
        newMessage = binding.root.findViewById(R.id.new_message)
        sendButton = binding.root.findViewById(R.id.send_button)
        sendButton!!.setOnClickListener(){
            sendMessage(newMessage!!.text.toString())
        }

        mMessages = ArrayList()
        recyclerView = binding.root.findViewById(R.id.message_list)
        recyclerView!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this);
        layoutManager.reverseLayout = true
        recyclerView!!.layoutManager = layoutManager
        recieveMessages(chatid!!, jwt)

        val ChatName: TextView = binding.root.findViewById<TextView>(R.id.chat_name)
        ChatName.text = chatName
        val imageView: ImageView = binding.root.findViewById<ImageView>(R.id.chat_image)
        Picasso.get().load(chatImg).placeholder(R.drawable.ic_profile).into(imageView)

        setContentView(binding.root)
    }



    private fun recieveMessages(chatid: Int, jwt: String) {
        var api = MessageApi()
        api.GetMessages(chatid, this, jwt)
    }

    public fun writeMessages(list: ArrayList<com.example.securitymessenger.Model.Message>){
        list.reverse()
        mMessages = list
        MessageAdapter = MessageAdapter(this, mMessages!!, false)
        recyclerView!!.adapter = MessageAdapter
        signalRListener = SignalRListener(jwt, 600000)
        signalRListener.startConnection { json -> recieveMessage(json) }
    }

    private fun sendMessage(message: String){
        if (message != ""){
            signalRListener.SendMessage(userId, chatid!!.toString(), message)
            newMessage!!.text.clear()
        }
    }

    override fun onDestroy() {
        signalRListener.stopConnection()
        super.onDestroy()
    }

    private fun recieveMessage(json: String){
        val message = Message()
        message.messageChatId = JSONObject(json).getString("ChatId").toInt()
        if(message.messageChatId == chatid) {
            message.messageUserId = JSONObject(json).getString("UserId").toString()
            message.messageUserName = JSONObject(json).getString("UserName").toString()
            message.messageText = JSONObject(json).getString("Message").toString()
            message.messageSendTime = JSONObject(json).getString("TimeSend").toString()
            (recyclerView!!.adapter as MessageAdapter).addMessage(message)
            recyclerView!!.smoothScrollToPosition(0)
        }
    }
}