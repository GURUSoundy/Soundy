package com.example.soundy

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MemoActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase
    lateinit var btnSaveMemo : Button
    lateinit var editMemo : EditText
    lateinit var btnBack : ImageButton
    lateinit var btnMypage : ImageButton
    lateinit var titleText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        editMemo = findViewById(R.id.editMemo)
        btnBack = findViewById(R.id.btnBack)
        btnMypage = findViewById(R.id.btnMypage)
        btnSaveMemo = findViewById(R.id.btnSaveMemo)
        titleText = findViewById(R.id.titleText)

        /* 뒤로가기 버튼 */
        btnBack.setOnClickListener {
            finish()
        }

        /* 마이페이지 버튼 */
        btnMypage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        /* ShowFileActivity에서 파일명, 디렉토리명 받아오기 */
        val fileName : String? = intent.getStringExtra("fileName")
        val dirName : String? = intent.getStringExtra("dirName")
        val filePath: String ?= intent.getStringExtra("filePath")

        titleText.setText(fileName)

        /* 파일 테이블에서 메모 값 읽어오기 */
        dbManager = DBManager(this, "File", null, 1)
        sqliteDB = dbManager.readableDatabase

        var cursor: Cursor = sqliteDB.rawQuery("select memo from File where fileName = '$fileName';", null)

        var validMemo = ""
        editMemo.setText("")

        /* 메모가 존재하는 경우 불러와서 보여주기 */
        while (cursor.moveToNext()) {
            validMemo = cursor.getString(0)
        }

        editMemo.setText(validMemo)
        Log.d("메모", "validMemo: $validMemo")

        cursor.close()

        /* 커서를 내용 끝에 위치시키기 */
        editMemo.setSelection(editMemo.length())

        /* 저장 버튼을 눌렀을 경우 메모 업데이트 */
        btnSaveMemo.setOnClickListener {
            val newMemo: String = editMemo.text.toString()
            sqliteDB = dbManager.writableDatabase
            sqliteDB.execSQL("UPDATE File SET memo = '$newMemo' WHERE fileName = '$fileName';")
            Toast.makeText(this, "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show()

            sqliteDB.close()

            val intent = Intent(this, ShowFileActivity::class.java)
            intent.putExtra("fileName", fileName)
            intent.putExtra("dirName", dirName)
            intent.putExtra("filePath", filePath)
            startActivity(intent)
            finish()
        }
    }
}