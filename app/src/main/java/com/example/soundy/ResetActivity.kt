package com.example.soundy

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

        btnBack = findViewById(R.id.btnBack)
        btnReset = findViewById(R.id.btnWithdrawal)

        btnBack.setOnClickListener{
            /* 초기화 화면에 들어오기 전의 화면으로 돌아가는 로직 구현 */
        }

        btnReset.setOnClickListener {
            /* 앱 내 데이터 초기화 로직 구현 */
        }
    }
}