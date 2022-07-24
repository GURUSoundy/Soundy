package com.example.soundy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var btnGoSignUp: Button
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

        /*
        btnGoSignUp.setOnClickListener {
            val intent = Intent(this, 회원가입액티비티::class.java)
            startActivity(intent)
        } */

    }
}