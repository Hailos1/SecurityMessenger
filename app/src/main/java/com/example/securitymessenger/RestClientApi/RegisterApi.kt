package com.example.securitymessenger.RestClientApi

import android.graphics.Bitmap
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody


class RegisterApi {

    val url : String = "http://securitychat.ru/api/Register"

    public fun register(userid: String, name: String, email: String, icon: String, password: String) : String {
        //val client = OkHttpClient()

        val params = RequestParams()
        params.put("UserId", userid)
        params.put("UserName", name)
        params.put("UserEmail", email)
        params.put("UserImg", icon)
        params.put("HashPassword", password)
        val client = AsyncHttpClient()
        var answer: String? = null
        client.post(url, params, object: AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                println(responseBody)
                answer = responseBody.contentToString()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                println(responseBody)
                if (error != null) {
                    println(error.message)
                }
                println(statusCode)
                println(headers)
                answer = responseBody.contentToString()
            }

        })
        return answer.toString()
    }
}