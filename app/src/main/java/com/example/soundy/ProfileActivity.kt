package com.example.soundy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class ProfileActivity : AppCompatActivity() {

    lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        /* 뒤로가기 버튼 클릭 리스너 */
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
    }
}