package com.example.securitymessenger.Adapters

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.securitymessenger.ChatActivity
import com.example.securitymessenger.Fragments.ChatsFragment
import com.example.securitymessenger.Fragments.SearchFragment
import com.example.securitymessenger.MainActivity
import com.example.securitymessenger.Model.User
import com.example.securitymessenger.R
import com.example.securitymessenger.RestClientApi.ChatsApi
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.prefs.Preferences

class UserAdapter(mContext: Context, mUsers: List<User>, isChatCheck: Boolean) :
    RecyclerView.Adapter<UserAdapter.ViewHolder?>() {

    private val mContext:Context
    private val mUsers: List<User>
    private var isChatCheck: Boolean
    private var firstUserId: String?
    private var jwt: String?
    private var settings: SharedPreferences? = null

    init {
        this.mContext = mContext
        this.mUsers = mUsers
        this.isChatCheck = isChatCheck
        this.settings = mContext.getSharedPreferences("account", AppCompatActivity.MODE_PRIVATE)
        this.firstUserId = settings!!.getString("userId", null)
        this.jwt = settings!!.getString("jwt", null)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var UserName: TextView
        var UserId: TextView
        var icon: CircleImageView

        init {
            UserName = itemView.findViewById(R.id.user_name_search)
            icon = itemView.findViewById(R.id.profile_image_search)
            UserId = itemView.findViewById(R.id.user_id_search)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.search_item_layout, viewGroup, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: User = mUsers[position]
        holder.UserName.text = user.userName.toString()
        Picasso.get().load(user.userImg).placeholder(R.drawable.ic_profile).into(holder.icon)
        holder.itemView.setOnClickListener {

            var secondUserId = user.userId.toString()
            var api = ChatsApi()
            api.AddChat(this.firstUserId!!, secondUserId, {bool -> AddChat(bool)}, this.jwt!!)
        }
    }
    private fun AddChat(boolean: Boolean){
        if (true){
            (mContext as MainActivity).setPage(0)
            ((mContext as MainActivity).supportFragmentManager.fragments.first() as ChatsFragment).refresh()
        }
    }
}