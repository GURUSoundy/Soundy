package com.example.soundy

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.viewpager2.widget.ViewPager2
import java.util.*

class SttQuizActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase
    lateinit var sttCard : TextView
    lateinit var btnBack : ImageButton
    lateinit var btnMypage : ImageButton
    lateinit var btnNext : Button
    lateinit var indicator1 : ImageView
    lateinit var indicator2 : ImageView
    lateinit var indicator3 : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stt_quiz)

        sttCard = findViewById(R.id.sttCard)
        btnBack = findViewById(R.id.btnBack)
        btnMypage = findViewById(R.id.btnMypage)
        btnNext = findViewById(R.id.btnNext)

//        indicator1 = findViewById(R.id.indicator1)
//        indicator2 = findViewById(R.id.indicator2)
//        indicator3 = findViewById(R.id.indicator3)

        /* 뒤로가기 버튼 */
        btnBack.setOnClickListener {
            val intent = Intent(this, ShowFileActivity::class.java)
            startActivity(intent)
        }

        /* 마이페이지 버튼 */
        btnMypage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        /* ShowFileActivity에서 파일명 받아오기 */
        val fileName : String? = intent.getStringExtra("fileName")

        /* 파일 테이블에서 STT 값 읽어오기 */
        dbManager = DBManager(this, "File", null, 1)
        sqliteDB = dbManager.readableDatabase

        var cursor: Cursor = sqliteDB.rawQuery("select * from File where fileName = '$fileName';", null)

        var validStt: String = ""

        while (cursor.moveToNext()) {
            var validFileName: String = cursor.getString(0)
            validStt = cursor.getString(3)
        }

        cursor.close()
        sqliteDB.close()

        /* STT 값이 존재 할 경우 */
        if(cursor.count == 1) {
            /* STT 값을 문단 단위로 Array에 담아 띄우기 */
            Toast.makeText(this, "복습 카드를 불러왔습니다.", Toast.LENGTH_SHORT).show()

            var sttToken = StringTokenizer(validStt, "\n")
            sttCard.text = sttToken.nextToken()

            btnNext.setOnClickListener {
                if (!(sttToken.hasMoreTokens())) {
                    sttToken = StringTokenizer(validStt, "\n")
                }
                sttCard.text = sttToken.nextToken()
            }

        } else {
            /* STT 값이 존재하지 않을 경우 */
            Toast.makeText(this, "STT 값이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
