package com.example.securitymessenger.RestClientApi

import android.graphics.Bitmap
import com.example.securitymessenger.Model.RegisterAnswer
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONObject


class RegisterApi {
    public fun register(userid: String, name: String, email: String, icon: String, password: String, func: (RegisterAnswer)->Unit) {
        val url : String = "http://securitychat.ru/api/Register/Register"
        //val client = OkHttpClient()
        val params = RequestParams()
        params.put("UserId", userid)
        params.put("UserName", name)
        params.put("UserEmail", email)
        params.put("UserImg", icon)
        params.put("HashPassword", password)
        val client = AsyncHttpClient()
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
                answer.HashCode = responseBody.getString("hashCode")
                answer.Message = responseBody.getString("message")
                func(answer)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: String?,
                error: Throwable?
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

    public fun tryCode(userid: String, name: String, email: String, icon: String, password: String, code: String, hashCode: String, func: (RegisterAnswer)->Unit) {
        val url : String = "http://securitychat.ru/api/Register/TryCode"
        //val client = OkHttpClient()
        val params = RequestParams()
        params.put("UserId", userid)
        params.put("UserName", name)
        params.put("UserEmail", email)
        params.put("UserImg", icon)
        params.put("code", code)
        params.put("hashCode", hashCode)
        params.put("HashPassword", password)
        val client = AsyncHttpClient()
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
                responseBody: String?,
                error: Throwable?
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