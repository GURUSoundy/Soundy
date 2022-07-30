package com.example.soundy

import android.app.DatePickerDialog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_file_list_detail.*
import java.util.*
import kotlin.collections.ArrayList

class FileListDetailActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase
    lateinit var btnBack: ImageButton
    lateinit var btnMypage: ImageButton
    lateinit var titleText: TextView
    lateinit var passedIntent: Intent
    lateinit var deadLineDate: TextView

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
        titleText = findViewById(R.id.titleText)
        deadLineDate = findViewById(R.id.deadLineDate)
        var dateString = ""

        /* FileListActivity에서 디렉토리 이름 받아오기 */
        passedIntent = getIntent()
        val dirName = passedIntent.getStringExtra("dirName").toString()
        titleText.text = dirName

        /* 디렉토리의 복습 마감 기한 받아오기 */
        dbManager = DBManager(this, "Directory", null, 1)
        sqliteDB = dbManager.readableDatabase
        var cursor1: Cursor = sqliteDB.rawQuery("select * from Directory where dirName = '$dirName';", null)

        while (cursor1.moveToNext()) {
            var endDate: String = cursor1.getString(1)
            deadLineDate.text = endDate
        }

        sqliteDB.close()
        dbManager.close()

        /* 날짜 클릭 시 복습 마감 기한 수정 가능 */
        deadLineDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${year}/${month+1}/${dayOfMonth}"

                dbManager = DBManager(this, "Directory", null, 1)
                sqliteDB = dbManager.writableDatabase
                sqliteDB.execSQL("UPDATE Directory SET endDate = '$dateString' where dirName = '$dirName';")

                sqliteDB.close()
                dbManager.close()

                /* 날짜 수정 후 새로고침 */
                val intent = getIntent()
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }

        btnBack.setOnClickListener {
            finish()
        }

        /* 마이페이지 이동 기능 */
        btnMypage=findViewById<ImageButton>(R.id.btnMypage)
        btnMypage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        /* 파일 목록(리사이클러 뷰) */
        dbManager = DBManager(this, "File", null, 1)
        sqliteDB = dbManager.readableDatabase

        /* DB에 있는 데이트들을 리스트에 넣기 */
        /* 디렉토리에 포함된 파일들만 보이도록 */
        var cursor: Cursor = sqliteDB.rawQuery("select * from File where dirName = '$dirName';", null)

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