package com.example.soundy

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_todo_list.*

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
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        /* 마이페이지 이동 기능 */
        btnMypage=findViewById<ImageButton>(R.id.btnMypage)
        btnMypage.setOnClickListener {
            val intent = Intent(this, mypage::class.java)
            startActivity(intent)
        }

        /* 투두 목록(리사이클러 뷰) */
        dbManager = DBManager(this, "TodoList", null, 1)
        sqliteDB = dbManager.readableDatabase

        /* DB에 있는 데이트들을 리스트에 넣기 */
        var cursor: Cursor = sqliteDB.rawQuery("select * from TodoList where date = '$date';", null)

        var todoList: ArrayList<Todos> = arrayListOf<Todos>()

        while (cursor.moveToNext()) {
            var todoText: String = cursor.getString(1)
            var todoChecked: Int = cursor.getInt(2)
            todoList.add(Todos(todoText, todoChecked))
        }

        sqliteDB.close()
        dbManager.close()

        rvTodoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTodoList.setHasFixedSize(true)

        rvTodoList.adapter = TodoAdapter(todoList)

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

            /* 투두리스트 추가 후 액티비티 새로고침(추가한 투두리스트 보이게) */
            val intent = getIntent()
            finish();
            startActivity(intent)
        }
    }

    override fun onCancelButtonClicked() {
    }
}