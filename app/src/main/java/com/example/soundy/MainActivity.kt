package com.example.soundy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var btnGoSignUp : Button
    lateinit var btnGoLogin : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* 자동 로그인 처리 */
        val auto = getSharedPreferences("autoLogin", Context.MODE_PRIVATE)
        var userId = auto.getString("userId", null)
        var userPW = auto.getString("userPW", null)
        var userNickName = auto.getString("userNickname", null)

        if (userId != null && userPW != null) {

            /* 아이디 넘기는 작업 추가 */

            /* 로그인 잘 완료되면 목록 액티비티로 넘어감 */
            val intent = Intent(this, FileListActivity::class.java)
            intent.putExtra("nickname", userNickName)
            startActivity(intent)
            finish()
        }

        btnGoLogin = findViewById(R.id.btnGoLogin)
        btnGoSignUp = findViewById(R.id.btnGoSignUp)

        btnGoLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnGoSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}