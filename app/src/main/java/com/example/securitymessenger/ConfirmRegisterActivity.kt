package com.example.securitymessenger

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.securitymessenger.Model.RegisterAnswer
import com.example.securitymessenger.RestClientApi.RegisterApi

class ConfirmRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_register)
        val hashCode = intent.getStringExtra("hashCode").toString()
        var registerButton = findViewById<Button>(R.id.confirm_register_button)
        registerButton.setOnClickListener(){
            var userid = intent.getStringExtra("userid").toString()
            var name = intent.getStringExtra("name").toString()
            var email = intent.getStringExtra("email").toString()
            var icon = intent.getStringExtra("icon").toString()
            var password = intent.getStringExtra("password").toString()
            var code = findViewById<EditText>(R.id.register_code).text.toString()
            var registerApi = RegisterApi()
            registerApi.tryCode(userid, name, email, icon, password, code, hashCode, {answer -> toLoginActivity(answer)})
        }
    }

    private fun toLoginActivity(answer: RegisterAnswer){
        if (answer.Answer) {
            val intent = Intent(this@ConfirmRegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        else{
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Ошибка")
                .setMessage(answer.Message)
                .setPositiveButton("OK",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            builder.create().show()
        }
    }
}