package com.example.soundy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText

class LoginActivity : AppCompatActivity() {
    lateinit var id: EditText
    lateinit var password: EditText
    lateinit var btnLogin: Button
    lateinit var autoLogin: CheckBox
    lateinit var btnForgetPassword: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        id = findViewById(R.id.id)
        password = findViewById(R.id.password)
        btnLogin = findViewById(R.id.btnLogin)
        autoLogin = findViewById(R.id.autoLogin)
        btnForgetPassword = findViewById(R.id.btnForgetPassword)

        /* 로그인 버튼 클릭 이벤트 핸들러 */
        btnLogin.setOnClickListener {
            /* id, password 넘겨서 DB에서 확인하는 로직 추가 */

            /* 자동로그인이 체크되었을 경우 로그인 정보 저장*/
            if (autoLogin.isChecked) {

            }

            /* 로그인 잘 완료되면 목록 액티비티로 넘어감
            val intent = Intent(this, listActivity::class.java)
            startActivity(intent) */
        }

        /* 비밀번호 재설정 버튼 클릭 이벤트 핸들러 */
        btnForgetPassword.setOnClickListener {

        }
    }
}