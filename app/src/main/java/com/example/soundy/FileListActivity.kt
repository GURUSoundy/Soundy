package com.example.soundy

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_file_list.*

class FileListActivity : AppCompatActivity(), CustomDialogInterface {
    lateinit var btnPlusDirectory: FloatingActionButton
    lateinit var btnMypage : ImageButton

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_list)

        /* 마이페이지 이동 기능 */
        btnMypage=findViewById<ImageButton>(R.id.btnMypage)
        btnMypage.setOnClickListener {
            val intent = Intent(this, mypage::class.java)
            startActivity(intent)
        }

        btnPlusDirectory = findViewById(R.id.btnPlusDirectory)

        btnPlusDirectory.setOnClickListener{
            val customDialog = CustomDialog(this, this)
            customDialog.show()
        }

        /* 디렉토리 목록(리사이클러 뷰) */
        dbManager = DBManager(this, "Directory", null, 1)
        sqliteDB = dbManager.readableDatabase

        /* DB에 있는 데이트들을 리스트에 넣기 */
        var cursor: Cursor = sqliteDB.rawQuery("select * from Directory;", null)

        var directoryList: ArrayList<Directorys> = arrayListOf<Directorys>()

        while (cursor.moveToNext()) {
            var dirName: String = cursor.getString(0)
            directoryList.add(Directorys(dirName))
        }

        sqliteDB.close()
        dbManager.close()

        rvDirectory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvDirectory.setHasFixedSize(true)

        rvDirectory.adapter = DirectoryAdapter(directoryList)

    }

    override fun onAddButtonClicked(dirName: String, endDate: String) {
        /* 디렉토리 추가 */
        if (dirName == "" || endDate == "") {
            Toast.makeText(this, "디렉토리 이름과 복습 마감 기한을 입력해주세요.", Toast.LENGTH_SHORT).show()
            /* 디렉토리 이름 입력 안됐을때 추가 버튼을 클릭하면 토스트 메시지와 함께 팝업 사라짐 -> 추후 수정 필요 */
        } else {
            dbManager = DBManager(this, "Directory", null, 1)
            sqliteDB = dbManager.writableDatabase
            sqliteDB.execSQL("INSERT INTO Directory VALUES('$dirName', '$endDate');")

            sqliteDB.close()
            dbManager.close()
            Toast.makeText(this, "디렉토리 추가 완료", Toast.LENGTH_SHORT).show()

            /* 디렉토리 추가 후 액티비티 새로고침(추가한 디렉토리 보이게) */
            val intent = getIntent()
            finish();
            startActivity(intent)
        }
    }

    override fun onCancelButtonClicked() {
    }
}