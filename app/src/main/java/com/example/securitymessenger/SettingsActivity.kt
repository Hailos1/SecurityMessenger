package com.example.securitymessenger

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.securitymessenger.Model.RegisterAnswer
import com.example.securitymessenger.RestClientApi.SettingsApi

class SettingsActivity : AppCompatActivity() {
    private var settings: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = getSharedPreferences("account", MODE_PRIVATE);
        val userid = settings!!.getString("userId", null)
        val name = settings!!.getString("userName", null)
        val icon = settings!!.getString("userImg", null)
        val jwt = settings!!.getString("jwt", null)
        setContentView(R.layout.activity_settings)
        val nameEditText: EditText = findViewById(R.id.settings_name)
        nameEditText.setText(name)
        val iconEditText: EditText = findViewById(R.id.settings_icon)
        iconEditText.setText(icon)
        val saveButton: Button = findViewById(R.id.save_settings_button)
        saveButton.setOnClickListener(){
            val api = SettingsApi()
            api.saveSettings(userid.toString(), nameEditText.text.toString(), iconEditText.text.toString(), jwt.toString(), {answer -> callback(answer)})
        }
    }

    fun callback(answer: RegisterAnswer){
        if (answer.Answer){
            finish()
        }
    }
}