package com.example.soundy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class ShowFileActivity : AppCompatActivity() {

    lateinit var fileName : TextView
    lateinit var btnShowUpload : Button
    lateinit var btnStt : ImageView
    lateinit var sttContent : TextView
    lateinit var btnQuiz : Button
    lateinit var btnRoutine : Button
    lateinit var btnBack : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_file)

        fileName = findViewById(R.id.fileName)
        btnShowUpload = findViewById(R.id.btnUpload)
        btnStt = findViewById(R.id.ivStt)
        sttContent = findViewById(R.id.sttContent)
        btnQuiz = findViewById(R.id.btnQuiz)
        btnRoutine = findViewById(R.id.btnRoutine)
        btnBack = findViewById(R.id.btnBack)


        fileName.setOnClickListener{
            /* 녹음 파일 재생. 텍스트뷰 말고 버튼으로 바꿀지? */
        }

        btnStt.setOnClickListener{
            /* stt 편집 페이지로 이동
            val intent = Intent(this, stt편집액티비티::class.java)
            startActivity(intent)
             */
        }

        btnQuiz.setOnClickListener {
            /* 퀴즈 페이지로 이동
            val intent = Intent(this, 퀴즈액티비티::class.java)
            startActivity(intent)
             */
        }

        btnRoutine.setOnClickListener {
            /* 복습 루틴 페이지로 이동 */
        }

        /* 뒤로 가기 버튼 클릭 시
        btnBack.setOnClickListener {
            val intent = Intent(this, 이전 액티비티::class.java)
            startActivity(intent)
        } */
    }
}