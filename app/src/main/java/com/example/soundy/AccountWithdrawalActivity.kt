package com.example.soundy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton

class AccountWithdrawalActivity : AppCompatActivity() {
    lateinit var btnBack: ImageButton
    lateinit var password: EditText
    lateinit var btnWithdrawal: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_withdrawal)

        password = findViewById(R.id.password)
        btnWithdrawal = findViewById(R.id.btnWithdrawal)

        /* 뒤로가기 버튼 클릭 리스너 */
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, mypage::class.java)
            startActivity(intent)
        }

        btnWithdrawal.setOnClickListener {
            /* password 입력 확인 후 계정 삭제 로직 구현 */
        }

    }
}