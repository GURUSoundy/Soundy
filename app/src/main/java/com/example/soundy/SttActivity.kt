package com.example.soundy

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SttActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase
    lateinit var btnSaveStt : Button
    lateinit var editStt : EditText
    lateinit var btnBack : ImageButton
    lateinit var btnMypage : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stt)

        editStt = findViewById(R.id.EditStt)
        btnBack = findViewById(R.id.btnBack)
        btnMypage = findViewById(R.id.btnMypage)
        btnSaveStt = findViewById(R.id.btnSaveStt)

        /* 뒤로가기 버튼 */
        btnBack.setOnClickListener {
            finish()
        }

        /* 마이페이지 버튼 */
        btnMypage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        /* ShowFileActivity에서 파일명 받아오기 */
        val fileName : String? = intent.getStringExtra("fileName")

        /* 파일 테이블에서 STT 값 읽어오기 */
        dbManager = DBManager(this, "File", null, 1)
        sqliteDB = dbManager.readableDatabase

        var cursor: Cursor = sqliteDB.rawQuery("select * from File where fileName = '$fileName';", null)

        var validStt: String = ""

        while (cursor.moveToNext()) {
            var validFileName: String = cursor.getString(0)
            validStt = cursor.getString(3)
        }

        /* STT 값이 존재 할 경우 */
        if(cursor.count == 1) {
            editStt.setText(validStt)
            Toast.makeText(this, "STT 값을 불러왔습니다.", Toast.LENGTH_SHORT).show()
        }else {
            /* STT 값이 존재하지 않을 경우 */
            Toast.makeText(this, "STT 값이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
        }

        cursor.close()

        /* 저장 버튼을 눌렀을 경우 STT 값 업데이트 */
        btnSaveStt.setOnClickListener {
            val newStt: String = editStt.text.toString()
            sqliteDB = dbManager.writableDatabase
            sqliteDB.execSQL("UPDATE File SET stt = '$newStt' WHERE fileName = '$fileName';")
            Toast.makeText(this, "STT 값이 저장되었습니다.", Toast.LENGTH_SHORT).show()

            sqliteDB.close()
        }
    }
}