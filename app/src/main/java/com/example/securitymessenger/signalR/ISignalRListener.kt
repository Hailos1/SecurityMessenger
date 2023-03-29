package com.example.securitychat.signalR

import com.google.gson.JsonElement
import com.google.gson.internal.LinkedTreeMap
import com.microsoft.signalr.Action
import com.microsoft.signalr.Action1
import com.microsoft.signalr.HubConnectionState

interface ISignalRListener {
    /**
     * Возвращает id подключения к SignalR.
     */
    val connectionId: String?

    /**
     * Возвращает статус подключения к SignalR.
     */
    val connectionState: HubConnectionState

    /**
     * Выполняет подключение к серверу SignalR.
     */
    fun startConnection(func: (String)->Unit)

    /**
     * Выполняет отключение от сервера SignalR.
     */
    fun stopConnection()

    fun SendMessage(user: String, message: String, chatid: String, fileid: Int = 0)

    fun GetMessages(user: String, chatid: String, start: Int, end: Int)

    fun ReceiveMessages()
}