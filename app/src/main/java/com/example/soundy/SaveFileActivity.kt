package com.example.soundy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class SaveFileActivity : AppCompatActivity() {

    lateinit var fileName : String

    lateinit var edtFileName : EditText
    lateinit var btnUpload : Button
    lateinit var btnStt : ImageView
    lateinit var sttContent : TextView
    lateinit var btnRoutine : Button
    lateinit var btnSave : Button
    lateinit var btnBack : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_file)

        edtFileName = findViewById(R.id.fileName)
        btnUpload = findViewById(R.id.btnUpload)
        btnStt = findViewById(R.id.ivStt)
        sttContent = findViewById(R.id.sttContent)
        btnRoutine = findViewById(R.id.btnRoutine)
        btnSave = findViewById(R.id.btnSave)
        btnBack = findViewById(R.id.btnBack)

        btnStt.setOnClickListener{
            /* stt 편집 페이지로 이동
            val intent = Intent(this, stt편집액티비티::class.java)
            startActivity(intent)
             */
        }

        btnSave.setOnClickListener {
            fileName = edtFileName.text.toString()

            Toast.makeText(this@SaveFileActivity, "$fileName 이 저장되었습니다.", Toast.LENGTH_SHORT).show()
        }

        /* 뒤로 가기 버튼 클릭 시
        btnBack.setOnClickListener {
            val intent = Intent(this, 이전 액티비티::class.java)
            startActivity(intent)
        } */
    }
}