package com.example.soundy

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room

class AccountWithdrawalActivity : AppCompatActivity() {
    lateinit var btnBack: ImageButton
    lateinit var pw: EditText
    lateinit var btnWithdrawal: Button

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase
    private lateinit var db : recordDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_withdrawal)

        pw = findViewById(R.id.enterPw)
        btnWithdrawal = findViewById(R.id.btnWithdrawal)

        /* 뒤로가기 버튼 클릭 리스너 */
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
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

                dbManager = DBManager(this, "Directory", null, 1)
                dbManager.deleteData("Directory")

                dbManager = DBManager(this, "TodoList", null, 1)
                dbManager.deleteData("TodoList")

                dbManager = DBManager(this, "File", null, 1)
                dbManager.deleteData("File")

                /* 녹음파일 table 삭제 */
                db= Room.databaseBuilder(
                    this, recordDatabase::class.java,
                    "audioRecords"
                ).build()

                val r = Runnable {
                    db.audioRecordDao().delete()
                }

                val thread = Thread(r)
                thread.start()

                sqliteDB.close()
                dbManager.close()

                /* !!!!! autoLogin 값 지워주는 로직  !!!!! */
                val unAuto = getSharedPreferences("autoLogin", Context.MODE_PRIVATE)
                val editor = unAuto.edit()
                editor.clear()
                editor.apply()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }

            cursor.close()
            sqliteDB.close()
            dbManager.close()
        }

    }
}