package com.example.securitymessenger.RestClientApi

import com.example.securitymessenger.LoginActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import gen._base._base_java__rjava_resources.srcjar.R.id.icon
import okhttp3.internal.wait

class AuthorizationApi {
    val url : String = "http://securitychat.ru/api/Authorization"
    public fun Authorization(UserEmail: String, UserPassword: String, func: ((String)->Unit)? = null){
        //val client = OkHttpClient()

        val params = RequestParams()
        params.put("UserEmail", UserEmail)
        params.put("UserPassword", UserPassword)
        val client = AsyncHttpClient()
        var answer: String? = null
        client.post(url, params, object: AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                if (func != null) {
                    func(String(responseBody))
                }
                answer = String(responseBody)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray,
                error: Throwable?
            ) {
                println(responseBody)
                println(statusCode)
                if (error != null) {
                    println(error.message)
                }
            }

        })
    }
}