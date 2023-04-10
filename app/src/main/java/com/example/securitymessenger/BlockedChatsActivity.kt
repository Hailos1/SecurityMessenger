package com.example.securitymessenger

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.ContentView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.securitymessenger.Adapters.BlockedChatAdapter
import com.example.securitymessenger.Adapters.ChatAdapter
import com.example.securitymessenger.Fragments.ChatsFragment
import com.example.securitymessenger.Model.Chat
import com.example.securitymessenger.RestClientApi.ChatsApi

class BlockedChatsActivity : AppCompatActivity() {

    private lateinit var jwt: String
    private lateinit var userid: String
    private var chatAdapter: BlockedChatAdapter? = null
    private var mChats: List<Chat>? = null
    private var recyclerView: RecyclerView? = null
    private var settings: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocked_chats)
        mChats = ArrayList()
        recyclerView = findViewById(R.id.blocked_chats)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        settings = getSharedPreferences("account", MODE_PRIVATE);
        userid = settings!!.getString("userId", null).toString()
        jwt = settings!!.getString("jwt", null).toString()
        recieveChats(userid, jwt)
    }

    private fun recieveChats(userid: String, jwt: String) {
        var api = ChatsApi()
        api.GetBlockChats(userid, jwt) { chats -> writeChats(chats) }
    }

    private fun writeChats(list: ArrayList<Chat>){
        this.mChats = list
        chatAdapter = BlockedChatAdapter(this, mChats!!, false, jwt, userid)
        recyclerView!!.adapter = chatAdapter
        chatAdapter!!.notifyDataSetChanged()
    }

    private fun finish(string: String){
        finish()
    }

    public fun dialogUnblockedChat(chatid: Int) : Boolean{
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Разблокировать чат")
            .setMessage("Вы уверены, что хотите разблокировать чат?")
            .setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, id ->
                    val api = ChatsApi()
                    api.UnBlockChat(userid, chatid, jwt) { str -> finish(str) }
                })
            .setNegativeButton("Отмена",
                DialogInterface.OnClickListener { dialog, id ->

                })
        builder.create().show()
        return true
    }
}