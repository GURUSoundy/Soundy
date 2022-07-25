package com.example.soundy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var btnGoSignUp: Button //회원가입 버튼
    lateinit var btnGoLogin : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGoSignUp = findViewById(R.id.btnGoSignUp)
        btnGoLogin = findViewById(R.id.btnGoLogin)

        btnGoLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnGoSignUp.setOnClickListener {
            val intent = Intent(this, signUp::class.java)
            startActivity(intent)
        }

    }
}