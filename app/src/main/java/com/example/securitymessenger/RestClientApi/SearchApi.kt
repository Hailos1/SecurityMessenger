package com.example.securitymessenger.RestClientApi

import com.example.securitymessenger.Fragments.SearchFragment
import com.example.securitymessenger.Fragments.SettingsFragment
import com.example.securitymessenger.LoginActivity
import com.example.securitymessenger.MainActivity
import com.example.securitymessenger.Model.User
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.awaitAll
import org.json.JSONArray
import org.json.JSONObject

class SearchApi {
    public fun findUsers(userid: String, fragment: SearchFragment) {
        println("findUses")
        val url : String = "http://securitychat.ru/api/User/GetUsersBySubStringId"
        val params = RequestParams()
        params.put("UserId", userid)
        val client = AsyncHttpClient()
        client.addHeader("Accept", "text/html,application/xhtml+xml,application/xml")
        client.get(url, params, object: JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: JSONArray?
            ) {
                println(responseBody.toString())

                val users = ArrayList<User>()
                if (responseBody != null) {
                    for (n in 1..responseBody.length()) {
                        var user: User = User()
                        user.userId=responseBody.getJSONObject(n-1).getString("userId")
                        user.userEmail=responseBody.getJSONObject(n-1).getString("userEmail")
                        user.userName=responseBody.getJSONObject(n-1).getString("userName")
                        user.userImg=responseBody.getJSONObject(n-1).getString("userImg")
                        users.add(user)
                    }
                }
                println(users)
                fragment.writeUsers(users)
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

    public fun findUsers(email: String, mainActivity: MainActivity) {
        val url : String = "http://securitychat.ru/api/User/GetUserByEmail"
        val params = RequestParams()
        params.put("UserEmail", email)
        val client = AsyncHttpClient()
        client.get(url, params, object: JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: JSONObject?
            ) {
                println(responseBody.toString())
                var user: User = User()
                if (responseBody != null) {
                        user.userId=responseBody.getString("userId")
                        user.userEmail=responseBody.getString("userEmail")
                        user.userName=responseBody.getString("userName")
                        user.userImg=responseBody.getString("userImg")
                }
                println(user)
                mainActivity.setUser(user)
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