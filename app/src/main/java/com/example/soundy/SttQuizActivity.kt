package com.example.soundy

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton

class SttQuizActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase

    lateinit var edtQuiz : EditText
    lateinit var btnHighlightQuiz : ImageButton
    lateinit var btnUnderscoreQuiz : ImageButton
    lateinit var btnConfirmQuiz : ImageButton
    lateinit var btnBack : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stt_quiz)

        edtQuiz = findViewById(R.id.edtQuiz)
        btnHighlightQuiz = findViewById(R.id.btnHighlightQuiz)
        btnUnderscoreQuiz = findViewById(R.id.btnUnderscoreQuiz)
        btnConfirmQuiz = findViewById(R.id.btnConfirmQuiz)
        btnBack = findViewById(R.id.btnBack)

        /* 뒤로가기 버튼 */
        btnBack.setOnClickListener {
            val intent = Intent(this, ShowFileActivity::class.java)
            startActivity(intent)
        }

        /* ShowFileActivity에서 파일명을 fileName 변수로 전달받았다고 가정 */
        val fileName = intent.getIntExtra("fileName",0)
        var cursor: Cursor = sqliteDB.rawQuery("select * from FILE where fileName = '$fileName';", null)

        while (cursor.moveToNext()) {
            var validFileName: String = cursor.getString(0)
            var validStt: String = cursor.getString(3)
        }
    }
}