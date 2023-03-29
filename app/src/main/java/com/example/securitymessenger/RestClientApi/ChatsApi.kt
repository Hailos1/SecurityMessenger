package com.example.securitymessenger.RestClientApi

import com.example.securitymessenger.Fragments.ChatsFragment
import com.example.securitymessenger.Model.Chat
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class ChatsApi {
    public fun GetChatsUserByEmail(email: String, fragment: ChatsFragment, jwt: String) {
        println(email)
        println("GetChatsUserByEmail")
        val url : String = "http://securitychat.ru/api/User/GetChatsUserByEmail"
        val params = RequestParams()
        params.put("UserEmail", email)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "Bearer " + jwt)
        client.addHeader("Accept", "text/html,application/xhtml+xml,application/xml")
        client.get(url, params, object: JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: JSONArray?
            ) {
                if (responseBody != null) {
                    println(responseBody.getJSONObject(0))
                }
                val chats = ArrayList<Chat>()
                if (responseBody != null) {
                    for (n in 1..responseBody.length()) {
                        var chat: Chat = Chat()
                        println(responseBody.getJSONObject(n-1))
                        chat.chatId = responseBody.getJSONObject(n-1).getString("chatId").toInt()
                        chat.chatName = responseBody.getJSONObject(n-1).getString("chatName")
                        chat.chatImg=responseBody.getJSONObject(n-1).getString("chatImg")
                        chat.isDialog=responseBody.getJSONObject(n-1).getString("isDialog").toBoolean()
                        chats.add(chat)
                    }
                }
                println(chats)
                fragment.writeChats(chats)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                error: Throwable?,
                responseBody: JSONArray?
            ) {
                println(responseBody)
                if (error != null) {
                    println(error.message)
                }
                println(statusCode)
                println(headers)
            }
        })
    }

    public fun AddChat(firstUser: String, secondUser: String, func: (Boolean)->Unit, jwt: String) {
        val url : String = "http://192.168.0.10:5000/api/Chat/CreateDialog"
        val params = RequestParams()
        params.put("firstUser", firstUser)
        params.put("secondUser", secondUser)
        val client = AsyncHttpClient()
        //client.addHeader("Authorization", "Bearer " + jwt)
        client.addHeader("Accept", "text/html,application/xhtml+xml,application/xml")
        client.post(url, params, object: JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: JSONArray?
            ) {
                println(responseBody)
                if (responseBody != null) {
                    func(responseBody.getBoolean(0))
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                error: Throwable?,
                responseBody: JSONArray?
            ) {
                println(responseBody)
                if (error != null) {
                    println(error.message)
                }
                println(statusCode)
                println(headers)
            }
        })
    }
}