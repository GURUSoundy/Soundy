package com.example.soundy

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class LoginActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase

    lateinit var id: EditText
    lateinit var password: EditText
    lateinit var btnLogin: Button
    lateinit var autoLogin: CheckBox
    lateinit var btnForgetPassword: Button

    lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /* 뒤로가기 버튼 클릭 리스너 */
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        /* 자동 로그인 처리 */
        val shared = getSharedPreferences("autoLogin", Context.MODE_PRIVATE)
        var userId = shared.getString("userId", null)
        var userPW = shared.getString("userPW", null)
        var userNickName = shared.getString("userNickname", null)

        /* !!!!! 로그아웃 페이지 만든 후 autoLogin 값 지워주는 로직 추가(노션 확인) !!!!! */

        if (userId != null && userPW != null) {
            Toast.makeText(this@LoginActivity, "${userNickName}님 환영합니다.", Toast.LENGTH_SHORT).show()

            /* 아이디 넘기는 작업 추가 */

            /* 로그인 잘 완료되면 목록 액티비티로 넘어감 */
            val intent = Intent(this, FileListActivity::class.java)
            startActivity(intent)
        } else {
            id = findViewById(R.id.stt)
            password = findViewById(R.id.password)
            btnLogin = findViewById(R.id.btnWithdrawal)
            autoLogin = findViewById(R.id.autoLogin)
            btnForgetPassword = findViewById(R.id.btnForgetPassword)

            dbManager = DBManager(this, "User", null, 1)
            sqliteDB = dbManager.readableDatabase

            /* 로그인 버튼 클릭 이벤트 핸들러 */
            btnLogin.setOnClickListener {
                /* id, password 넘겨서 DB에서 확인하는 로직 */
                var str_id: String = id.text.toString()
                var str_password: String = password.text.toString()
                var nickname: String = ""

                var cursor: Cursor = sqliteDB.rawQuery("select * from User where id = '$str_id' and password = '$str_password';", null)

                while (cursor.moveToNext()) {
                    var validId: String = cursor.getString(0)
                    var validPassword: String = cursor.getString(1)
                    nickname = cursor.getString(2)
                }

                if (cursor.count == 1) {
                    Toast.makeText(this@LoginActivity, "${nickname}님 환영합니다.", Toast.LENGTH_SHORT).show()

                    /* 아이디 넘기는 작업 추가 */

                    /* 로그인 잘 완료되면 목록 액티비티로 넘어감 */
                    val intent = Intent(this, FileListActivity::class.java)
                    startActivity(intent)

                    cursor.close()
                    sqliteDB.close()

                } else {
                    /* 아이디나 비밀번호를 입력하지 않았을 경우 / 아이디 또는 비밀번호가 틀렸을 경우 */
                    if (str_id.isEmpty() || str_password.isEmpty()) {
                        Toast.makeText(this@LoginActivity, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                /* 자동로그인이 체크되었을 경우 로그인 정보 저장*/
                if (autoLogin.isChecked) {
                    val auto = getSharedPreferences("autoLogin", Context.MODE_PRIVATE)
                    val editor = auto.edit()
                    editor.putString("userId", str_id)
                    editor.putString("userPW", str_password)
                    editor.putString("userNickname", nickname)
                    editor.apply()
                }
            }

            /* 비밀번호 재설정 버튼 클릭 이벤트 핸들러 */
            btnForgetPassword.setOnClickListener {
                Toast.makeText(this@LoginActivity, "비밀번호 재설정.", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, ResetPasswordAvtivity::class.java)
                startActivity(intent)
            }
        }




    }

}