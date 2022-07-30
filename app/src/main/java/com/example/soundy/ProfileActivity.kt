package com.example.soundy

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class ProfileActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase
    lateinit var imgProfile : ImageView
    lateinit var nickname : EditText
    lateinit var oldPwd : EditText
    lateinit var newPwd : EditText
    lateinit var btnProfileSave : Button
    lateinit var btnBack: ImageButton
    lateinit var btnNicknameSave : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dbManager = DBManager(this, "User", null, 1)
        sqliteDB = dbManager.readableDatabase

        imgProfile = findViewById(R.id.imgProfile)
        nickname = findViewById(R.id.nickname)
        oldPwd = findViewById(R.id.oldPwd)
        newPwd = findViewById(R.id.newPwd)
        btnProfileSave = findViewById(R.id.btnProfileSave)
        btnBack = findViewById(R.id.btnBack)
        btnNicknameSave = findViewById(R.id.btnNicknameSave)

        /* 뒤로가기 버튼 */
        btnBack.setOnClickListener {
            finish()
        }

        /* mypage에서 사용자 id 받아오기 */
        val strId : String? = intent.getStringExtra("userId")

        /* DB 읽어오기 */
        var cursor: Cursor = sqliteDB.rawQuery("select * from User where id = '$strId';", null)

        var validId: String = ""
        var validNickname : String = ""
        var validPwd : String = ""
        var newNickname : String = ""

        while (cursor.moveToNext()) {
            validId = cursor.getString(0)
            validNickname = cursor.getString(1)
            validPwd = cursor.getString(2)
        }

        /* 닉네임 불러오기 */
        if(cursor.count == 1) {
            nickname.setText(validNickname)
            Toast.makeText(this, "닉네임을 불러왔습니다.", Toast.LENGTH_SHORT).show()
        }else {
            /* STT 값이 존재하지 않을 경우 */
            Toast.makeText(this, "존재하지 않는 ID입니다.", Toast.LENGTH_SHORT).show()
        }

        cursor.close()
        sqliteDB.close()

        /* 닉네임 변경하기 */
        btnNicknameSave.setOnClickListener {
            var newNickname : String = nickname.text.toString()
            sqliteDB = dbManager.writableDatabase
            sqliteDB.execSQL("UPDATE User SET nickname = '$newNickname' WHERE id = '$validId';")
            sqliteDB.close()

            Toast.makeText(this, "닉네임을 변경했습니다.", Toast.LENGTH_SHORT).show()
        }

        /* 비밀번호 변경하기 */
        btnProfileSave.setOnClickListener {
            var strOldPwd: String = oldPwd.text.toString()
            var strNewPwd: String = newPwd.text.toString()

            if (strOldPwd.length == 0 || strNewPwd.length == 0 ) {
                /* 모든 칸을 입력하지 않았을 경우 */
                Toast.makeText(this, "기존 비밀번호, 새로운 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (strOldPwd != validPwd) {
                /* 기존 비밀번호가 틀렸을 경우 */
                Toast.makeText(this, "기존 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
            } else {
                /* 정상적인 경우 비밀변호 변경 */
                sqliteDB = dbManager.writableDatabase
                sqliteDB.execSQL("UPDATE User SET password = '$strNewPwd' WHERE id = '$strId';")
                sqliteDB.close()

                Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()

            }
        }
    }
}