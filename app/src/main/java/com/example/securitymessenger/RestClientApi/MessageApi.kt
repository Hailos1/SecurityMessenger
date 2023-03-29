package com.example.securitymessenger.RestClientApi

import com.example.securitymessenger.ChatActivity
import com.example.securitymessenger.Model.Message
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class MessageApi {
    public fun GetMessages(chatId: Int, activity: ChatActivity, jwt: String) {
        val url : String = "http://securitychat.ru/api/Message/GetMessages"
        val params = RequestParams()
        params.put("ChatId", chatId)
        params.put("start", 0)
        params.put("end", 100)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "Bearer $jwt")
        client.addHeader("Accept", "text/html,application/xhtml+xml,application/xml")
        client.get(url, params, object: JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: JSONArray?
            ) {
                val messages = ArrayList<Message>()
                if (responseBody != null) {
                    for (n in 1..responseBody.length()) {
                        var message: Message = Message()
                        println(responseBody.getJSONObject(n-1))
                        message.messageUserId = responseBody.getJSONObject(n-1).getString("userId")
                        message.messageText = responseBody.getJSONObject(n-1).getString("message")
                        messages.add(message)
                    }
                }
                println(messages)
                activity.writeMessages(messages)
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