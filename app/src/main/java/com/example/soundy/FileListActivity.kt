package com.example.soundy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_file_list.*

class FileListActivity : AppCompatActivity() {
    lateinit var btnPlusDirectory: FloatingActionButton
    /* 목록(상세) 테스트용 변수 */
    lateinit var directoryName1: TextView
    lateinit var btnMypage : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_list)

        directoryName1 = findViewById(R.id.directoryName1)
        btnPlusDirectory = findViewById(R.id.btnPlusDirectory)

        directoryName1.setOnClickListener {
            val intent = Intent(this@FileListActivity, FileListDetailActivity::class.java)
            startActivity(intent)
        }

        btnPlusDirectory.setOnClickListener{
            Toast.makeText(this, "디렉토리 추가", Toast.LENGTH_SHORT).show()
            /* 디렉토리 추가 로직 구현 */
        }

        /* 마이페이지 이동 버튼 */
        btnMypage=findViewById<ImageButton>(R.id.btnMypage)
        btnMypage.setOnClickListener {
            val intent = Intent(this, mypage::class.java)
            startActivity(intent)
        }
    }
}