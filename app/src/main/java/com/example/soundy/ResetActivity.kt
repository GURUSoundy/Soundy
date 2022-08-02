package com.example.soundy

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.room.Room

class ResetActivity : AppCompatActivity() {
    lateinit var btnBack: ImageButton
    lateinit var btnReset: Button
    lateinit var dbManager: DBManager
    private lateinit var db : recordDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        btnReset = findViewById(R.id.btnWithdrawal)

        /* 뒤로가기 버튼 클릭 리스너 */
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        btnReset.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("정말로 초기화하시겠습니까?")
                .setPositiveButton("초기화",
                DialogInterface.OnClickListener{ dialog, id ->
                    /* 앱 내 데이터 초기화 로직 */
                    dbManager = DBManager(this, "Directory", null, 1)
                    dbManager.deleteData("Directory")

                    dbManager = DBManager(this, "TodoList", null, 1)
                    dbManager.deleteData("TodoList")

                    dbManager = DBManager(this, "File", null, 1)
                    dbManager.deleteData("File")

                    /* 녹음파일 table 삭제 */
                    db= Room.databaseBuilder(
                        this, recordDatabase::class.java,
                        "audioRecords"
                    ).build()

                    val r = Runnable {
                        db.audioRecordDao().delete()
                    }

                    val thread = Thread(r)
                    thread.start()

                    val intent = Intent(this, FileListActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                })
                .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->

                })
            builder.show()

        }
    }
}