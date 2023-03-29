package com.example.securitymessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.securitymessenger.RestClientApi.RegisterApi

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var registerButton = findViewById<Button>(R.id.register_button)
        registerButton.setOnClickListener(){
            var userid = findViewById<EditText>(R.id.user_userid_register).text.toString()
            var name = findViewById<EditText>(R.id.user_name_register).text.toString()
            var email = findViewById<EditText>(R.id.user_email_register).text.toString()
            var icon = findViewById<EditText>(R.id.user_icon_register).text.toString()
            var password = findViewById<EditText>(R.id.user_password_register).text.toString()
            var registerApi = RegisterApi()
            registerApi.register(userid, name, email, icon, password)
        }
    }
}