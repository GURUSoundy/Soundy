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

class FileListActivity : AppCompatActivity(), CustomDialogInterface {
    lateinit var btnPlusDirectory: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_list)

        btnPlusDirectory = findViewById(R.id.btnPlusDirectory)

        btnPlusDirectory.setOnClickListener{
            val customDialog = CustomDialog(this, this)
            customDialog.show()
        }
    }

    override fun onAddButtonClicked() {
        /* 디렉토리 추가 */
        Toast.makeText(this, "확인", Toast.LENGTH_SHORT).show()
    }

    override fun onCancelButtonClicked() {
        Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show()
    }
}