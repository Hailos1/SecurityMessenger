package com.example.securitymessenger.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.securitymessenger.Model.Message
import com.example.securitymessenger.R

class MessageAdapter(mContext: Context, mMessages: ArrayList<Message>, isChatCheck: Boolean) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder?>() {
    private val mContext: Context
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
        var UserName: TextView
        var MessageContent: TextView

        init {
            UserName = itemView.findViewById(R.id.user_name_inchat)
            //icon = itemView.findViewById(R.id.profile_image_inchat)
            MessageContent = itemView.findViewById(R.id.message_id)
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
        holder.UserName.text = message.messageUserId
        holder.MessageContent.text = message.messageText
    }
}