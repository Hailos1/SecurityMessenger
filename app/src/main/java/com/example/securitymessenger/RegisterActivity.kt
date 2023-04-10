package com.example.securitymessenger

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.securitymessenger.Fragments.ChatsFragment
import com.example.securitymessenger.Model.RegisterAnswer
import com.example.securitymessenger.RestClientApi.ChatsApi
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
            registerApi.register(userid, name, email, icon, password, {answer -> toLoginActivity(answer)})
        }
    }

    private fun toLoginActivity(answer: RegisterAnswer){
        if (answer.Answer) {
            val intent = Intent(this@RegisterActivity, ConfirmRegisterActivity::class.java)
            intent.putExtra("hashCode",answer.HashCode)
            intent.putExtra("userid", findViewById<EditText>(R.id.user_userid_register).text.toString())
            intent.putExtra("name", findViewById<EditText>(R.id.user_name_register).text.toString())
            intent.putExtra("email", findViewById<EditText>(R.id.user_email_register).text.toString())
            intent.putExtra("icon", findViewById<EditText>(R.id.user_icon_register).text.toString())
            intent.putExtra("password", findViewById<EditText>(R.id.user_password_register).text.toString())
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