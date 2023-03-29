package com.example.securitychat.signalR

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import com.microsoft.signalr.TransportEnum
import io.reactivex.Single
import okhttp3.WebSocket
import okhttp3.internal.ws.WebSocketProtocol

class SignalRListener (jwt: String, chatid: Int): ISignalRListener {
    var jwt = jwt
    var chatid = chatid

    private val hubConnection = HubConnectionBuilder.create("http://securitychat.ru/messengerHub")
        .withTransport(TransportEnum.LONG_POLLING)
        .withAccessTokenProvider(Single.just(jwt))
        .build()

    override val connectionId: String?
        get() = hubConnection.connectionId
    override val connectionState: HubConnectionState
        get() = hubConnection.connectionState

    override fun startConnection(func: (String)->Unit) {
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

    override fun GetMessages(user: String, chatid: String, start: Int, end: Int) {
        TODO("Not yet implemented")
    }

    override fun ReceiveMessages() {
        TODO("Not yet implemented")
    }
}
