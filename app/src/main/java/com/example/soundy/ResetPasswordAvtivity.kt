package com.example.soundy

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast


class ResetPasswordAvtivity() : AppCompatActivity() {

    lateinit var dbManager : DBManager
    lateinit var sqliteDB : SQLiteDatabase

    lateinit var btnBack: ImageButton
    lateinit var id : EditText
    lateinit var pwd : EditText
    lateinit var pwdCheck : EditText
    lateinit var btnResetPwd : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password_avtivity)

        dbManager = DBManager(this, "User", null, 1)
        sqliteDB = dbManager.readableDatabase

        btnBack = findViewById(R.id.btnBack)
        id = findViewById(R.id.stt)
        pwd = findViewById(R.id.pwd)
        pwdCheck = findViewById(R.id.pwdCheck)
        btnResetPwd = findViewById(R.id.btnModify)

        /* 뒤로가기 버튼 */
        btnBack.setOnClickListener {
            finish()
        }

        /* 비밀번호 변경 */
        btnResetPwd.setOnClickListener {
            var strId: String = id.text.toString()
            var strPwd: String = pwd.text.toString()
            var strPwdCheck: String = pwdCheck.text.toString()

            /* 모든 칸을 입력하지 않았을 경우 */
            if (strId.length == 0 || strPwd.length == 0 || strPwdCheck.length == 0) {
                Toast.makeText(this, "아이디, 비밀번호, 비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (strPwd != strPwdCheck) {
                Toast.makeText(this, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }


            /* id가 존재하는지 DB 확인 */
            var cursor: Cursor = sqliteDB.rawQuery("select * from User where id = '$strId';", null)

            while (cursor.moveToNext()) {
                var validId: String = cursor.getString(0)
            }

            /* 비밀번호 변경 */
            if (cursor.count == 1) {
                /* 아이디가 존재할 경우 */
                sqliteDB = dbManager.writableDatabase
                sqliteDB.execSQL("UPDATE User SET password = '$strPwd' WHERE id = '$strId';")
                cursor.close()
                sqliteDB.close()

                Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()

            } else { /* 아이디가 존재하지 않을 경우 */
                Toast.makeText(this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
