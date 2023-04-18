package com.example.securitymessenger

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.securitychat.signalR.SignalRListener
import com.example.securitymessenger.Adapters.MessageAdapter
import com.example.securitymessenger.Model.Message
import com.example.securitymessenger.RestClientApi.MessageApi
import com.example.securitymessenger.databinding.ActivityChatBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.security.Permission
import java.security.Permissions
import java.util.*


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
    private var clipButton: ImageView? = null

    private var additionid: Int? = null

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
        clipButton = binding.root.findViewById(R.id.clip_button)
        clipButton!!.setOnClickListener(){
            if (additionid == null) {
                val photoPickerIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent, 1)
            }
            else{
                clipButton!!.setImageResource(R.drawable.clip)
                additionid = null
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            //var bitmap: Bitmap? = null
            //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data!!.data);
            //val stream = ByteArrayOutputStream()
            //bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream)
            
            val selectedImageUri = data?.data // получаем URI выбранного изображения
            val projection = arrayOf(MediaStore.Images.Media.DATA) // задаем столбцы, которые следует получить
            val cursor = selectedImageUri?.let { contentResolver.query(it, projection, null, null, null) } // выполняем запрос к MediaStore, чтобы получить путь к файлу
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            var filePath: String? = null
            if (cursor?.moveToFirst() == true && columnIndex != null) { // если запрос вернул результаты, ищем столбец, где содержится путь к файлу
                filePath = cursor.getString(columnIndex)
            }
            cursor?.close()

            var api = MessageApi()
            api.SendFile(File(filePath), jwt){
                id -> recieveAddition(id)
            }
            clipButton!!.setImageResource(R.drawable.ic_attach_file)
        }
    }

    private fun recieveAddition(id: Int){
        additionid = id
    }

    private fun recieveMessages(chatid: Int, jwt: String) {
        var api = MessageApi()
        api.GetMessages(chatid, this, jwt)
    }

    public fun writeMessages(list: ArrayList<com.example.securitymessenger.Model.Message>){
        list.reverse()
        mMessages = list
        MessageAdapter = MessageAdapter(this, mMessages!!, false, userId)
        recyclerView!!.adapter = MessageAdapter
        signalRListener = SignalRListener(jwt, 600000)
        signalRListener.startConnection { json -> recieveMessage(json) }
    }

    private fun sendMessage(message: String){
        if (message != "" || additionid != null){
            if (additionid == null) {
                signalRListener.SendMessage(userId, chatid!!.toString(), message)
            }
            else{
                signalRListener.SendMessage(userId, chatid!!.toString(), message, additionid!!)
            }
            newMessage!!.text.clear()
            additionid = null
            clipButton!!.setImageResource(R.drawable.clip)
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
            message.pathsAddition = JSONObject(json).getString("pathsAddition").toString()
            (recyclerView!!.adapter as MessageAdapter).addMessage(message)
            recyclerView!!.smoothScrollToPosition(0)
        }
    }
}