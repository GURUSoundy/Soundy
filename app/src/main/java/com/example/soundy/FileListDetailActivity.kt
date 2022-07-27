package com.example.soundy

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_file_list_detail.*

class FileListDetailActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase
    lateinit var btnBack: ImageButton
    lateinit var btnMypage: ImageButton

    /* 일단 캐시 디렉토리에 recording.3gp라는 이름으로 저장 */
    val recordingFilePath: String by lazy {
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }

    /* 오디오 기능 사용 명시 */
    val requiredPermissions = arrayOf(
        android.Manifest.permission.RECORD_AUDIO
    )

    var recorder: MediaRecorder ?= null
    var player: MediaPlayer ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_list_detail)

        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, FileListActivity::class.java)
            startActivity(intent)
        }

        /* 마이페이지 이동 기능 */
        btnMypage=findViewById<ImageButton>(R.id.btnMypage)
        btnMypage.setOnClickListener {
            val intent = Intent(this, mypage::class.java)
            startActivity(intent)
        }

        /* 파일 목록(리사이클러 뷰) */
        dbManager = DBManager(this, "File", null, 1)
        sqliteDB = dbManager.readableDatabase

        /* DB에 있는 데이트들을 리스트에 넣기 */
        var cursor: Cursor = sqliteDB.rawQuery("select * from File;", null)

        var fileList: ArrayList<Files> = arrayListOf<Files>()

        while (cursor.moveToNext()) {
            var fileName: String = cursor.getString(0)
            fileList.add(Files(fileName))
        }

        sqliteDB.close()
        dbManager.close()

        rvFile.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvFile.setHasFixedSize(true)

        rvFile.adapter = FileAdapter(fileList)
    }
}