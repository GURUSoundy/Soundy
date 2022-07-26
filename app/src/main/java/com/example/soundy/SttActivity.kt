package com.example.soundy

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class SttActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase

    lateinit var editStt : EditText
    lateinit var btnHighlight : ImageButton
    lateinit var btnUnderscore : ImageButton
    lateinit var btnEraser : ImageButton
    lateinit var btnSave : ImageButton
    lateinit var btnBack : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stt)

        editStt = findViewById(R.id.EditStt)
        btnHighlight = findViewById(R.id.btnHighlight)
        btnUnderscore = findViewById(R.id.btnUnderscore)
        btnEraser =findViewById(R.id.btnEraser)
        btnBack = findViewById(R.id.btnBack)

        /* 뒤로가기 버튼 */
        btnBack.setOnClickListener {
            val intent = Intent(this, ShowFileActivity::class.java)
            startActivity(intent)
        }

        /* 파일 테이블에서 stt 값 읽어오기 */
        dbManager = DBManager(this, "File", null, 1)
        sqliteDB = dbManager.readableDatabase

        /* ShowFileActivity에서 파일명을 fileName 변수로 전달받았다고 가정 */
        val fileName = intent.getIntExtra("fileName",0)
        var cursor: Cursor = sqliteDB.rawQuery("select * from FILE where fileName = '$fileName';", null)

        while (cursor.moveToNext()) {
            var validFileName: String = cursor.getString(0)
            var validStt: String = cursor.getString(3)
        }


        editStt.addTextChangedListener(object : TextWatcher {
            /* afterTextChanged : 텍스트 변경 후에 호출 */
            override fun afterTextChanged(p0: Editable?) {
                TODO("Not yet implemented")
            }

            /* beforeTextChanged : 텍스트 변경 전에 호출 */
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (cursor.count == 1) {
                    /* STT 값이 존재 할 경우 */


                } else {
                    /* STT 값이 존재하지 않을 경우 */
                    Toast.makeText(this, "STT 값이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            /* onTextChanged : 텍스트 변경 중에 호출 */
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }


        })

        /* 하이라이트 버튼을 눌렀을 경우 */
        btnHighlight.setOnClickListener {

        }

        /* 밑줄 버튼을 눌렀을 경우 */
        btnUnderscore.setOnClickListener {

        }

        /* 지우개 버튼을 눌렀을 경우 */
        btnEraser.setOnClickListener {

        }

        /* 저장 버튼을 눌렀을 경우 값 업데이트 */
        btnSave.setOnClickListener {
            sqliteDB = dbManager.writableDatabase
            //sqliteDB.execSQL("UPDATE FILE SET stt = '$newStt' fileName = '$fileName';")

            cursor.close()
            sqliteDB.close()
        }


    }
}
