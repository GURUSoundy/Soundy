package com.example.soundy

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_sign_up.*

class signUp : AppCompatActivity() {

    // private val root: Any
//    private lateinit var binding: signUp
//        private lateinit var localDB: DBManager
//        val DATABASE_VERSION = 1
//        val DATABASE_NAME = "DBManager.db"
//
//
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            binding = signUp.inflate(layoutInflater)    // 뷰 바인딩
//            val view = binding.root
//            setContentView(view)
//
//
//            localDB= DBManager(this, DATABASE_NAME,null, DATABASE_VERSION) // SQLite 모듈 생성
//
//            binding.btnSignup.setOnClickListener { view->
//                if(binding.enterId.text.isEmpty()||binding.enterPw.text.isEmpty()||binding.rePw.text.isEmpty()){// 값이 전부 입력되지 않은경우
//                    Toast.makeText(this,"값을 전부 입력해주세요..",Toast.LENGTH_LONG).show()
//                }else{
//
//                    if(binding.rePw.text.toString().equals(binding.rePw.text.toString())){//패스워드/패스워드 확인이 일치
//                        if(localDB.checkIdExist(binding.enterId.text.toString())){// 아이디 중복 확인
//                            Toast.makeText(this,"아이디가 이미 존재합니다.",Toast.LENGTH_LONG).show()
//                        }else{// 존재하는 아이디
//                            localDB.signupUser(binding.enterId.text.toString(),binding.enterPw.text.toString())
//                        }
//                    }else{ // 패스워드/패스워드 확인이 일치하지 않음
//                        Toast.makeText(this,"패스워드가 틀렸습니다.",Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//        }
//  }


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

        var dbManager = DBManager(this, "memInfoDB", null, 1)

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
                dbManager.insertUser(nick, id, pw)
                Log.d(TAG, "회원정보 삽입")
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()


                // 메인 화면으로 이동
                val intent = Intent(this, MainActivity::class.java)
                   startActivity(intent)
            }
        }
    }
}
