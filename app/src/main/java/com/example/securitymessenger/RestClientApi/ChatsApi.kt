package com.example.securitymessenger.RestClientApi

import com.example.securitymessenger.Fragments.ChatsFragment
import com.example.securitymessenger.Model.Chat
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONStringer

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
                val chats = ArrayList<Chat>()
                if (responseBody != null) {
                    for (n in 0..responseBody.length() - 1) {
                        var chat: Chat = Chat()
                        println(responseBody.getJSONObject(n))
                        chat.chatId = responseBody.getJSONObject(n).getString("chatId").toInt()
                        chat.chatName = responseBody.getJSONObject(n).getString("chatName")
                        chat.chatImg=responseBody.getJSONObject(n).getString("chatImg")
                        chat.isDialog=responseBody.getJSONObject(n).getString("isDialog").toBoolean()
                        chats.add(chat)
                    }
                }
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
        val url : String = "http://securitychat.ru/api/Chat/CreateDialog"
        val params = RequestParams()
        params.put("firstUser", firstUser)
        params.put("secondUser", secondUser)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "Bearer " + jwt)
        client.addHeader("Accept", "text/html,application/xhtml+xml,application/xml")
        client.post(url, params, object: JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: JSONObject?
            ) {
                if (responseBody != null) {
                    func(responseBody.getString("answer").toBoolean())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                error: Throwable?,
                responseBody: JSONObject?
            ) {
                if (error != null) {
                    println(error.message)
                }
                println(responseBody)
                println(statusCode)
                println(headers)
            }
        })
    }

    public fun BlockChat(UserId: String, ChatId: Int, func: (Boolean)->Unit, jwt: String) {
        val url : String = "http://securitychat.ru/api/Chat/BlockChat"
        val params = RequestParams()
        params.put("userId", UserId)
        params.put("chatId", ChatId)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "Bearer " + jwt)
        client.addHeader("Accept", "text/html,application/xhtml+xml,application/xml")
        client.post(url, params, object: JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: JSONObject?
            ) {
                println(responseBody)
                if (responseBody != null) {
                    func(responseBody.getString("answer").toBoolean())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                error: Throwable?,
                responseBody: JSONObject?
            ) {
                if (error != null) {
                    println(error.message)
                }
                println(responseBody)
                println(statusCode)
                println(headers)
            }
        })
    }

    public fun GetBlockChats(userid: String, jwt: String, func: (ArrayList<Chat>) -> Unit) {
        val url : String = "http://securitychat.ru/api/Chat/GetBlockChats"
        val params = RequestParams()
        params.put("userId", userid)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "Bearer " + jwt)
        client.addHeader("Accept", "text/html,application/xhtml+xml,application/xml")
        client.post(url, params, object: JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: JSONArray?
            ) {
                val chats = ArrayList<Chat>()
                if (responseBody != null) {
                    for (n in 0..responseBody.length() - 1) {
                        var chat: Chat = Chat()
                        println(responseBody.getJSONObject(n))
                        chat.chatId = responseBody.getJSONObject(n).getString("chatId").toInt()
                        chat.chatName = responseBody.getJSONObject(n).getString("chatName")
                        chat.chatImg=responseBody.getJSONObject(n).getString("chatImg")
                        chat.isDialog=responseBody.getJSONObject(n).getString("isDialog").toBoolean()
                        chats.add(chat)

                    }
                }
                func(chats)
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

    public fun UnBlockChat(userid: String, chatid: Int, jwt: String, func: (String) -> Unit) {
        val url : String = "http://securitychat.ru/api/Chat/UnBlockChat"
        val params = RequestParams()
        params.put("userId", userid)
        params.put("chatId", chatid)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "Bearer " + jwt)
        client.addHeader("Accept", "text/html,application/xhtml+xml,application/xml")
        client.post(url, params, object: JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: JSONObject?
            ) {
                func("true")
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