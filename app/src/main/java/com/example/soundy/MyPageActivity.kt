package com.example.soundy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class MyPageActivity : AppCompatActivity() {

    lateinit var btnProfile : Button
    lateinit var btnAlarm : Button
    lateinit var btnReset : Button
    lateinit var btnLogout : Button
    lateinit var btnQuit : Button
    lateinit var btnBack: ImageButton
    lateinit var nickname: TextView
    lateinit var myEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        overridePendingTransition(R.anim.horizon_enter_from_right, R.anim.none)

        btnProfile =findViewById(R.id.btnProfile)
        btnAlarm = findViewById(R.id.btnAlarm)
        btnReset= findViewById(R.id.btnReset)
        btnLogout= findViewById(R.id.btnLogout)
        btnQuit= findViewById(R.id.btnQuit)
        btnBack = findViewById(R.id.btnBack)
        nickname = findViewById(R.id.nickname)
        myEmail = findViewById(R.id.myEmail)

        val userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val userNickName = userInfo.getString("userNickName", null)
        val userId = userInfo.getString("userId", null)

        nickname.text = userNickName.toString()
        myEmail.text = userId.toString()

        /* 뒤로가기 버튼 클릭 리스너 */
        btnBack.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.none, R.anim.horizon_exit_to_right)
        }
        /*!!!!!각 페이지에 맞게 이름 수정해야 함!!!!!!*/

        /* 프로필수정 버튼 */
        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("userId", userId)
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
            /* !!!!! autoLogin 값 지워주는 로직  !!!!! */
            val unAuto = getSharedPreferences("autoLogin", Context.MODE_PRIVATE)
            val editor = unAuto.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        }
        /* 탈퇴 버튼 */
        btnQuit.setOnClickListener {
            val intent = Intent(this, AccountWithdrawalActivity::class.java)
            startActivity(intent)
        }
    }
}