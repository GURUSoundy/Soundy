package com.example.soundy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*

class SignUpActivity : AppCompatActivity() {

    lateinit var enterNick: EditText
    lateinit var enterId: EditText
    lateinit var enterPw: EditText
    lateinit var rePw: EditText
    lateinit var btnSignup: Button
    lateinit var btnBack: ImageButton

    val TAG: String = "Register"
    //var isExistBlank = false
    //ar isPWSame = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //dbManager=DBManager(this)

        /* 뒤로가기 버튼 클릭 리스너 */
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        enterNick = findViewById(R.id.enterNick)
        enterId = findViewById(R.id.enterId)
        enterPw = findViewById(R.id.enterPw)
        rePw = findViewById(R.id.rePw)
        btnSignup = findViewById(R.id.btnSignup)

        var dbManager = DBManager(this, "User", null, 1)

        btnSignup.setOnClickListener {
            Log.d(TAG, "회원가입 버튼 클릭")

            val nick = enterNick.text.toString()
            val id = enterId.text.toString()
            val pw = enterPw.text.toString()
            val pw_re = rePw.text.toString()

            /* 유저가 항목을 다 채우지 않았을 경우*/
            if (nick.length == 0 || id.length == 0 || pw.length == 0 || pw_re.length == 0) {

                Toast.makeText(this, "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show()
            }

            /* 비밀번호 다를 경우*/
            else if (pw != pw_re) {
                Toast.makeText(this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
            }

            /* 유저가 항목을 다 채웠고 비밀번호도 같을 경우*/
            else {

                // 회원가입 성공 토스트 메세지 띄우기
                dbManager.insertUser(id, nick, pw)
                Log.d(TAG, "회원정보 삽입")
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()


                // 메인 화면으로 이동
                val intent = Intent(this, MainActivity::class.java)
                   startActivity(intent)
            }
        }
    }
}
