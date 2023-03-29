package com.example.securitymessenger

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.example.securitymessenger.Model.User

class WelcomeActivity : AppCompatActivity() {

    var User : User? = null
    var settings: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = getSharedPreferences("account", MODE_PRIVATE);
        var jwt = settings!!.getString("jwt",null);
        var useremail = settings!!.getString("useremail",null);
        println(jwt)
        if (jwt != null && useremail != null){
            val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
            intent.putExtra("jwt", jwt);
            intent.putExtra("email", useremail)
            startActivity(intent)
            finish()
        }
        setContentView(R.layout.activity_welcome)
        var EnterButton = findViewById<Button>(R.id.welcome_enter_button)
        EnterButton.setOnClickListener{
            val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        var RegisterButton = findViewById<Button>(R.id.welcome_register_button)
        RegisterButton.setOnClickListener{
            val intent = Intent(this@WelcomeActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()



        if (User != null){
            val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}