package com.example.soundy

import android.app.DatePickerDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.util.*

class ShowFileActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase

    lateinit var fileName : String
    lateinit var dirName : String
    lateinit var sttContent : String
    lateinit var routine : String

    lateinit var titleText : TextView
    lateinit var tvFileName : TextView
    lateinit var btnShowUpload : Button
    lateinit var btnStt : ImageView
    lateinit var tvSttContent : TextView
    lateinit var btnQuiz : Button
    lateinit var btnRoutine : Button
    lateinit var btnBack : ImageButton
    lateinit var btnMypage : ImageButton
    lateinit var btnPlay : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_file)

        titleText = findViewById(R.id.titleText)
        tvFileName = findViewById(R.id.tvFileName)
        btnShowUpload = findViewById(R.id.btnUpload)
        btnStt = findViewById(R.id.ivStt)
        tvSttContent = findViewById(R.id.tvSttContent)
        btnQuiz = findViewById(R.id.btnQuiz)
        btnRoutine = findViewById(R.id.btnRoutine)
        btnBack = findViewById(R.id.btnBack)
        btnPlay = findViewById(R.id.btnPlay)

        dbManager = DBManager(this, "File", null, 1)
        sqliteDB = dbManager.readableDatabase

        var play_status = false

        /* FileListDetail 에서 파일명(녹음파일명) 받아옴 */
        titleText.setText(intent.getStringExtra("fileName").toString())
        tvFileName.setText(intent.getStringExtra("fileName").toString())
        dirName = intent.getStringExtra("dirName").toString()

        /* 녹음 파일 저장된 경로
        var mediaplayer : MediaPlayer?= MediaPlayer.create(this, R.raw.sample) */

        btnPlay.setOnClickListener{
            /* 녹음 파일 재생, 재생 상태인지 일시 정지 상태인지에 따라 */
            if(play_status == false){
                btnPlay.setImageResource(R.drawable.pause_icon)
                /* 녹음 파일(fileName) 재생 코드
                mediaplayer?.start() */
                play_status = true
            } else {
                btnPlay.setImageResource(R.drawable.play_icon)
                /* 녹음 파일 일시 정지 코드
                mediaplayer?.pause() */
                play_status = false
            }
        }

        btnShowUpload.setOnClickListener {
            // 업로드 했던 파일을 보여줌
        }

        /* db에서 (녹음)파일명으로 stt 내용을 불러옴 */
        //sttContent = sqliteDB.rawQuery("select stt from File where fileName = '$name';", null).toString()
        sttContent = "아직.."
        tvSttContent.setText(sttContent)

        btnStt.setOnClickListener{
            /* stt 편집 페이지로 이동 */
            Toast.makeText(this@ShowFileActivity, fileName, Toast.LENGTH_SHORT).show()

            val intent = Intent(this, SttActivity::class.java)
            intent.putExtra("fileName", fileName)
            startActivity(intent)
        }

        btnQuiz.setOnClickListener {
            /* 퀴즈 페이지로 이동 */
            val intent = Intent(this, SttQuizActivity::class.java)
            startActivity(intent)
        }

        btnRoutine.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                routine = "${year}-${month+1}-${dayOfMonth}"
                // db에 수정한 날짜 저장

                Toast.makeText(this@ShowFileActivity, "${year}년 ${month+1}월 ${dayOfMonth}일로 수정되었습니다.", Toast.LENGTH_SHORT).show()
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }

        /* 마이페이지 이동 기능 */
        btnMypage=findViewById<ImageButton>(R.id.btnMypage)
        btnMypage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        /* 뒤로 가기 버튼 클릭 시 */
        btnBack.setOnClickListener {
            finish()
        }
    }
}