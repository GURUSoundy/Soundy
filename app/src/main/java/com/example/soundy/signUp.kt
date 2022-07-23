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

class signUp : AppCompatActivity() {

//    lateinit var dbManager: DBManager
//    lateinit var sqlitedb : SQLiteDatabase

    lateinit var enterNick: EditText
    lateinit var enterId: EditText
    lateinit var enterPw: EditText
    lateinit var rePw: EditText
    lateinit var btnSignup: Button
    lateinit var btnBack: ImageButton

    val TAG: String = "Register"
    var isExistBlank = false
    var isPWSame = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        /* 뒤로가기 버튼 클릭 리스너 */
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        enterNick = findViewById(R.id.enterNick)
        enterId = findViewById(R.id.enterId)
        enterPw = findViewById(R.id.enterPw)
        rePw = findViewById(R.id.rePw)
        btnSignup = findViewById(R.id.btnSignup)

        //dbManager = DBManager(this,"memInfoDB", null, 1)

        btnSignup.setOnClickListener {
                Log.d(TAG, "회원가입 버튼 클릭")

                val nick = enterNick.text.toString()
                val id = enterId.text.toString()
                val pw = enterPw.text.toString()
                val pw_re = rePw.text.toString()

                /* 유저가 항목을 다 채우지 않았을 경우*/
                if(nick.isEmpty() || id.isEmpty() || pw.isEmpty() || pw_re.isEmpty()){
                    isExistBlank = true
                }
                else{
                    if(pw == pw_re){
                        isPWSame = true
                    }
                }

                /* 유저가 항목을 다 채웠고 비밀번호도 같을 경우*/
                if(!isExistBlank && isPWSame){

                    // 회원가입 성공 토스트 메세지 띄우기
                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                    // 유저가 입력한 id, pw를 쉐어드에 저장
                    val sharedPreference = getSharedPreferences("file name", Context.MODE_PRIVATE)
                    val editor = sharedPreference.edit()
                    editor.putString("id", id)
                    editor.putString("pw", pw)
                    editor.apply()

                    // 로그인 화면으로 이동
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("intent_name", id)
                    startActivity(intent)

                }
                // 상태에 따라 다른 다이얼로그 띄워주기
                else{
                    // 작성 안한 항목이 있을 경우
                    if(isExistBlank){
                        dialog("blank")
                    }
                    // 입력한 비밀번호가 다를 경우
                    else if(!isPWSame){
                        dialog("not same")
                    }
                }

//            sqlitedb=dbManager.writableDatabase
//            sqlitedb.execSQL("INSERT INTO memInfoDB VALUES('"+nick+"', '"+id+"' ,'"+pw+"', '"+pw_re+"')")
//            sqlitedb.close()

            }
        }

        /*회원가입 실패시 다이얼로그를 띄워주는 메소드*/
        fun dialog(type: String){
            val dialog = AlertDialog.Builder(this)

            /*작성 안한 항목이 있을 경우*/
            if(type.equals("blank")){
                dialog.setTitle("회원가입 실패")
                dialog.setMessage("입력란을 모두 작성해주세요")
            }
            /*입력한 비밀번호가 다를 경우*/
            else if(type.equals("not same")){
                dialog.setTitle("회원가입 실패")
                dialog.setMessage("비밀번호가 다릅니다")
            }

            val dialog_listener = object: DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when(which){
                        DialogInterface.BUTTON_POSITIVE ->
                            Log.d(TAG, "다이얼로그")
                    }
                }
            }
            dialog.setPositiveButton("확인",dialog_listener)
            dialog.show()
        }
    }
