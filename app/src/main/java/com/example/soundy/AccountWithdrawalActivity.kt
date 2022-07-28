package com.example.soundy

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class AccountWithdrawalActivity : AppCompatActivity() {
    lateinit var btnBack: ImageButton
    lateinit var pw: EditText
    lateinit var btnWithdrawal: Button

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_withdrawal)

        pw = findViewById(R.id.enterPw)
        btnWithdrawal = findViewById(R.id.btnWithdrawal)

        /* 뒤로가기 버튼 클릭 리스너 */
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, mypage::class.java)
            startActivity(intent)
        }

        btnWithdrawal.setOnClickListener {
            /* password 입력 확인 후 계정 삭제 로직 구현 */
            val userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
            var userId = userInfo.getString("userId", null)
            var strPassword: String = pw.text.toString()

            dbManager = DBManager(this, "User", null, 1)
            sqliteDB = dbManager.readableDatabase

            var cursor: Cursor = sqliteDB.rawQuery("select * from User where id = '$userId' and password = '$strPassword';", null)
            Log.d("탈퇴", "비밀번호: $strPassword")

            while (cursor.moveToNext()) {
                var validId: String = cursor.getString(0)
                var validPassword: String = cursor.getString(2)
            }

            /* 입력한 비밀번호가 맞다면 */
            if (cursor.count == 1) {
                sqliteDB.execSQL("DELETE FROM User WHERE id = '$userId'")
                sqliteDB.close()
                dbManager.close()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }

            cursor.close()
            sqliteDB.close()
            dbManager.close()
        }

    }
}