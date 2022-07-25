package com.example.soundy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class ResetActivity : AppCompatActivity() {
    lateinit var btnBack: ImageButton
    lateinit var btnReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        btnReset = findViewById(R.id.btnWithdrawal)

        /* 뒤로가기 버튼 클릭 리스너 */
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, mypage::class.java)
            startActivity(intent)
        }

        btnReset.setOnClickListener {
            /* 앱 내 데이터 초기화 로직 구현 */
        }
    }
}