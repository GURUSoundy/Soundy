package com.example.soundy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_mypage.*

class mypage : AppCompatActivity() {

    lateinit var btnBack : Button
    lateinit var btnProfile : Button
    lateinit var btnAlarm : Button
    lateinit var btnReset : Button
    lateinit var btnLogout : Button
    lateinit var btnQuit : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        btnBack = findViewById(R.id.btnBack)
        btnProfile =findViewById(R.id.btnProfile)
        btnAlarm = findViewById(R.id.btnAlarm)
        btnReset= findViewById(R.id.btnReset)
        btnLogout= findViewById(R.id.btnLogout)
        btnQuit= findViewById(R.id.btnQuit)

        /* 뒤로가기 버튼 클릭 리스너 */
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        /*!!!!!각 페이지에 맞게 이름 수정해야 함!!!!!!*/

        /* 프로필수정 버튼 */
        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        /* 알람시간설정 버튼 */
        btnAlarm.setOnClickListener {
            val intent = Intent(this, AlarmActivity::class.java)
            startActivity(intent)
        }
        /* 초기화 버튼 */
        btnReset.setOnClickListener {
            val intent = Intent(this, ResetActivity::class.java)
            startActivity(intent)
        }
        /* 로그아웃 버튼 */
        btnLogout.setOnClickListener {
 //          val intent = Intent(this, Logout::class.java)
            startActivity(intent)
        }
        /* 탈퇴 버튼 */
        btnQuit.setOnClickListener {
            val intent = Intent(this, AccountWithdrawalActivity::class.java)
            startActivity(intent)
        }
    }
}