package com.example.securitymessenger.Adapters

import android.content.Context
import android.text.Layout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.view.MarginLayoutParamsCompat
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.example.securitymessenger.Model.Message
import com.example.securitymessenger.R
import com.squareup.picasso.Picasso

class MessageAdapter(mContext: Context, mMessages: ArrayList<Message>, isChatCheck: Boolean, userId: String) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder?>() {
    private val mContext: Context
    private val userId = userId
    val mMessages: ArrayList<Message>
    private var isChatCheck: Boolean

    init {
        this.mContext = mContext
        this.mMessages = mMessages
        this.isChatCheck = isChatCheck
    }

    fun addMessage(message: Message){
        mMessages.add(0, message)
        notifyItemInserted(0)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //var icon: CircleImageView
        var Img: ImageView
        var UserName: TextView
        var MessageContent: TextView
        var MessageTime: TextView

        init {
            UserName = itemView.findViewById(R.id.user_name_inchat)
            Img = itemView.findViewById(R.id.message_img)
            //icon = itemView.findViewById(R.id.profile_image_inchat)
            MessageContent = itemView.findViewById(R.id.message_id)
            MessageTime = itemView.findViewById(R.id.message_time)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.message_item_layout, viewGroup, false)
        return MessageAdapter.ViewHolder(view)
    }

    fun updateAdapter(items: ArrayList<Message>){
        // обновляем список
        mMessages.clear()
        mMessages.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mMessages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message: Message = mMessages[position]
        holder.UserName.text = message.messageUserName
        holder.MessageContent.text = message.messageText
        holder.MessageTime.text = message.messageSendTime.toString()
        if (message.pathsAddition != "null"){
            holder.Img.visibility = VISIBLE
            println(message.pathsAddition)
            Picasso.get().load(message.pathsAddition).resize(1920, 0).onlyScaleDown().into(holder.Img)
        }
        else{
            holder.Img.visibility = View.GONE
        }
    }
}