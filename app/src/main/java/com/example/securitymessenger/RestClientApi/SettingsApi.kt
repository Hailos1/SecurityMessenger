package com.example.securitymessenger.RestClientApi

import com.example.securitymessenger.Model.RegisterAnswer
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class SettingsApi {
    public fun saveSettings(userid: String, name: String, icon: String, jwt: String, func: (RegisterAnswer)->Unit) {
        val url : String = "http://securitychat.ru/api/Settings"
        //val client = OkHttpClient()
        val params = RequestParams()
        params.put("userid", userid)
        params.put("name", name)
        params.put("icon", icon)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "Bearer $jwt")
        client.addHeader("Accept", "text/html,application/xhtml+xml,application/xml")
        client.post(url, params, object: JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: JSONObject?
            ) {
                println(responseBody)
                val answer = RegisterAnswer()
                answer.Answer = responseBody!!.getString("answer").toBoolean()
                answer.Message = responseBody.getString("message")
                func(answer)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                error: Throwable?,
                responseBody: JSONObject?
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