package com.example.securitychat.signalR

import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import com.microsoft.signalr.TransportEnum
import io.reactivex.rxjava3.core.Single
import java.util.*

class SignalRListener (jwt: String, keepAlive: Long): ISignalRListener {
    var jwt = jwt
    var keepAlive = keepAlive

    private val hubConnection = HubConnectionBuilder.create("http://securitychat.ru/messengerHub")
        .withTransport(TransportEnum.WEBSOCKETS)
        .withAccessTokenProvider(Single.just(jwt))
        .build()
    private lateinit var func: (String)->Unit
    override val connectionId: String?
        get() = hubConnection.connectionId
    override val connectionState: HubConnectionState
        get() = hubConnection.connectionState

    override fun startConnection(func: (String)->Unit) {
        this.func = func
        time()
        hubConnection.start().blockingAwait()
        hubConnection.on("ReceiveMessage", fun (json: String){
            func(json)
        }, String::class.java)
    }

    override fun stopConnection() {
        hubConnection.stop()
    }

    override fun SendMessage(user: String, message: String, chatid: String, fileid: Int) {
        hubConnection.invoke("SendMessage", user, chatid, message, fileid)
    }

    fun time(){
        val myTimer = Timer()
        myTimer.schedule(object : TimerTask() {
            override fun run() {
                tickTimer()
            }
        }, 0, 500)
    }

    fun tickTimer(){
        if (connectionState == HubConnectionState.DISCONNECTED){
            hubConnection.start().blockingAwait()
            println("reopen")
        }
    }

    override fun GetMessages(user: String, chatid: String, start: Int, end: Int) {
        TODO("Not yet implemented")
    }

    override fun ReceiveMessages() {
        TODO("Not yet implemented")
    }
}
