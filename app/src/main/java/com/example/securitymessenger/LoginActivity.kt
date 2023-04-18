package com.example.securitymessenger

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.securitymessenger.RestClientApi.AuthorizationApi


class LoginActivity : AppCompatActivity() {

    public var jwt: String? = null
    private var useremail: String? = null
    var settings: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        settings = getSharedPreferences("account", MODE_PRIVATE);
        jwt = settings!!.getString("jwt",null);
        useremail = settings!!.getString("useremail",null);
        println(jwt)
        if (jwt != null && useremail != null){
            enterInSystem(jwt)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener(){
            useremail = findViewById<EditText>(R.id.user_email_login).text.toString()
            var password = findViewById<EditText>(R.id.user_password_login).text.toString()
            var Api = AuthorizationApi()
            Api.Authorization(useremail!!, password, {jwt -> enterInSystem(jwt)})
        }
    }

    public  fun enterInSystem(jwt: String?){
        this.jwt = jwt
        if (jwt != null){
            val prefEditor = settings!!.edit()
            prefEditor.putString("jwt", jwt)
            prefEditor.putString("useremail", useremail)
            prefEditor.apply()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.putExtra("jwt", jwt)
            intent.putExtra("email", useremail)
            startActivity(intent)
            finish()
        }
    }
}