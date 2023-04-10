package com.example.securitymessenger.Fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.securitymessenger.Adapters.ChatAdapter
import com.example.securitymessenger.Model.Chat
import com.example.securitymessenger.R
import com.example.securitymessenger.RestClientApi.ChatsApi

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatsFragment(email: String, jwt: String, userId: String) : Fragment() {
    private var email: String = email
    public var jwt: String = jwt
    public var userId: String = userId
    private var chatAdapter: ChatAdapter? = null
    private var mChats: List<Chat>? = null
    private var recyclerView: RecyclerView? = null
    private var mView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate(R.layout.fragment_chats, container, false)
        mChats = ArrayList()
        recyclerView = mView!!.findViewById(R.id.chat_list)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recieveChats(email, jwt)

        (mView as SwipeRefreshLayout).setOnRefreshListener{
            (mView as SwipeRefreshLayout).isRefreshing = true
            recieveChats(email, jwt)
        }

        return mView!!
    }

    public fun refresh(){
        (mView as SwipeRefreshLayout).isRefreshing = true
        recieveChats(email, jwt)
    }

    private fun recieveChats(email: String, jwt: String) {
        var api = ChatsApi()
        println(email)
        api.GetChatsUserByEmail(email, this, jwt)
    }

    public fun writeChats(list: List<Chat>){
        this.mChats = list
        chatAdapter = ChatAdapter(requireContext(), mChats!!, false, jwt, userId)
        recyclerView!!.adapter = chatAdapter
        chatAdapter!!.notifyDataSetChanged()
        (mView as SwipeRefreshLayout).isRefreshing = false
    }
}