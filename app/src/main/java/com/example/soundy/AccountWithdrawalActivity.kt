package com.example.soundy

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

        btnBack = findViewById(R.id.btnBack)
        password = findViewById(R.id.password)
        btnWithdrawal = findViewById(R.id.btnWithdrawal)

        btnBack.setOnClickListener{
            /* 뒤로가기 버튼 로직 구현 */
        }

        btnWithdrawal.setOnClickListener {
            /* password 입력 확인 후 계정 삭제 로직 구현 */
        }

    }
}