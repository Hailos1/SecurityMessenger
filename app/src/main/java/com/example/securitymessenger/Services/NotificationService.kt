package com.example.securitymessenger.Services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.securitychat.signalR.SignalRListener
import com.example.securitymessenger.Model.Message
import com.example.securitymessenger.MyApplication
import com.example.securitymessenger.R
import org.json.JSONObject
import java.util.*


class NotificationService : Service() {
    private val dictionary: MutableMap<String, Int> = mutableMapOf()
    private lateinit var notificationManager: NotificationManager
    private lateinit var signalRListener: SignalRListener
    private lateinit var jwt: String
    private lateinit var userId: String
    var settings: SharedPreferences? = null

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    private fun parseMessage(json: String) : Message{
        val message = Message()
        message.messageChatId = JSONObject(json).getString("ChatId").toInt()
        message.messageUserId = JSONObject(json).getString("UserId").toString()
        message.messageUserName = JSONObject(json).getString("UserName").toString()
        message.messageText = JSONObject(json).getString("Message").toString()
        message.messageSendTime = JSONObject(json).getString("TimeSend").toString()
        return message
    }

    private fun initNotifyManager(){
        notificationManager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        val CHANNEL_ID: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CHANNEL_ID = "my_channel"
            val name: CharSequence = "SecurityMessengerChannel"
            val description = "This is SecurityMessengerChannel for notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.enableLights(true)
            mChannel.lightColor = Color.MAGENTA
            mChannel.description = description
            mChannel.enableVibration(true)
            mChannel.setShowBadge(true)
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun sendNotifictation(json: String){
        val message = parseMessage(json)
        if (userId != message.messageUserId) {
            if (!dictionary.containsKey(message.messageUserId)) {
                dictionary.put(message.messageUserId!!, dictionary.count() + 1)
            }
            //if (!(applicationContext as MyApplication).isAppForeground()) {
                val builder: NotificationCompat.Builder =
                    NotificationCompat.Builder(this, "my_channel")
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launch)
                        .setContentTitle(message.messageUserName)
                        .setContentText("Сообщение: " + message.messageText.toString().take(50))
                val notification: Notification = builder.build()
                notificationManager.notify(dictionary.get(message.messageUserId)!!, notification)
            //}
        }
    }

    override fun onCreate() {
        super.onCreate()
        initNotifyManager()
        settings = getSharedPreferences("account", MODE_PRIVATE);
        jwt = settings!!.getString("jwt",null).toString();
        userId = settings!!.getString("userId",null).toString();
        signalRListener = SignalRListener(jwt!!, 86400000)
        signalRListener.startConnection { json -> sendNotifictation(json) }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
}