package com.example.soundy

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_file_list_detail.*
import kotlinx.android.synthetic.main.activity_todo_list.*
import kotlinx.android.synthetic.main.activity_trycal.*
import java.text.SimpleDateFormat

class ToDoListActivity : AppCompatActivity(), TodoDialogInterface {

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase
    lateinit var titleText: TextView
    lateinit var btnBack: ImageButton
    lateinit var btnMypage: ImageButton
    lateinit var passedIntent: Intent
    lateinit var todoDate: TextView

    lateinit var btnPlusTodo: FloatingActionButton
    lateinit var date: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        /* TryCalActivity에서 date 받아오기 */
        passedIntent = getIntent()
        val month = passedIntent.getStringExtra("month").toString()
        val day = passedIntent.getStringExtra("day").toString()
        todoDate = findViewById(R.id.todoDate)
        date = "${month}.${day}"
        todoDate.text = date

        /* 투두리스트 추가 버튼 */
        btnPlusTodo = findViewById(R.id.btnPlusTodo)
        btnPlusTodo.setOnClickListener {
            val TodoDialog = TodoDialog(this, this)
            TodoDialog.show()
        }

        /*뒤로가기 버튼*/
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
            val intent = Intent(this, TryCalActivity::class.java)
            startActivity(intent)
        }

        /* 마이페이지 이동 기능 */
        btnMypage=findViewById<ImageButton>(R.id.btnMypage)
        btnMypage.setOnClickListener {
            val intent = Intent(this, mypage::class.java)
            startActivity(intent)
        }

//        /* 파일 목록(리사이클러 뷰) */
//        dbManager = DBManager(this, "TodoList", null, 1)
//        sqliteDB = dbManager.readableDatabase

    }

    override fun onAddButtonClicked(todo: String) {
        /* 투두리스트 추가 */
        if (todo == "") {
            Toast.makeText(this, "투두리스트 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            /* 디렉토리 이름 입력 안됐을때 추가 버튼을 클릭하면 토스트 메시지와 함께 팝업 사라짐 -> 추후 수정 필요 */
        } else {
            dbManager = DBManager(this, "TodoList", null, 1)
            sqliteDB = dbManager.writableDatabase
            sqliteDB.execSQL("INSERT INTO TodoList VALUES('$date', '$todo', 0);")

            sqliteDB.close()
            dbManager.close()
            Toast.makeText(this, "투두리스트 추가 완료", Toast.LENGTH_SHORT).show()

            /* 디렉토리 추가 후 액티비티 새로고침(추가한 디렉토리 보이게) */
            val intent = getIntent()
            finish();
            startActivity(intent)
        }
    }

    override fun onCancelButtonClicked() {
    }
}