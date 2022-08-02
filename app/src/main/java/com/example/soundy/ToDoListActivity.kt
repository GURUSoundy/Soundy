package com.example.soundy

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
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
    lateinit var achieveProgress: ProgressBar

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognitionListener: RecognitionListener

    lateinit var btnPlusTodo: FloatingActionButton
    lateinit var date: String
    var tvResult : String = ""

    lateinit var todoLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        /* CalendarActivity에서 date 받아오기 */
        passedIntent = intent
        val month = passedIntent.getStringExtra("month").toString()
        val day = passedIntent.getStringExtra("day").toString()
        todoDate = findViewById(R.id.todoDate)
        date = "${month}.${day}"
        todoDate.text = date

        /* 성취도 계산 */
        achieveProgress = findViewById(R.id.achieveProgress)
        dbManager = DBManager(this, "TodoList", null, 1)
        sqliteDB = dbManager.readableDatabase
        var todoNum: Int = 0
        var completedTodo: Int = 0

        var cursor1: Cursor = sqliteDB.rawQuery("select * from TodoList where date = '$date';", null)

        while (cursor1.moveToNext()) {
            todoNum += 1
            if (cursor1.getInt(2) == 1) {
                completedTodo += 1
            }
        }

        if (todoNum != 0) {
            achieveProgress.progress = ((completedTodo.toDouble() / todoNum.toDouble()) * 100).toInt()
        } else {
            achieveProgress.progress = 0
        }

        cursor1.close()
        sqliteDB.close()
        dbManager.close()

        /* 투두리스트 추가 버튼 */
        btnPlusTodo = findViewById(R.id.btnPlusTodo)
        btnPlusTodo.setOnClickListener {

            val sttIntent = Intent(this, ToDoSTTActivity::class.java)
            sttIntent.putExtra("date", date)
            startActivity(sttIntent)
        }

        /* 뒤로가기 버튼 */
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
            finish()
        }

        /* 마이페이지 이동 기능 */
        btnMypage=findViewById<ImageButton>(R.id.btnMypage)
        btnMypage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
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

        cursor.close()
        sqliteDB.close()
        dbManager.close()

        rvTodoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTodoList.setHasFixedSize(true)

        rvTodoList.adapter = TodoAdapter(todoList)

        todoLayout = findViewById(R.id.todoLayout)
        todoLayout.setOnClickListener {
            val intent = intent
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    /* 음성 녹음 함수 */
    private fun setListener() {
        recognitionListener = object: RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Toast.makeText(applicationContext, "음성 녹음을 시작합니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}

            /* 에러 발생 시 호출 */
            override fun onError(error: Int) {
                var message: String

                when (error) {
                    SpeechRecognizer.ERROR_AUDIO ->
                        message = "오디오 에러"
                    SpeechRecognizer.ERROR_CLIENT ->
                        message = "클라이언트 에러"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                        message = "퍼미션 없음"
                    SpeechRecognizer.ERROR_NETWORK ->
                        message = "네트워크 에러"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                        message = "네트워크 타임아웃"
                    SpeechRecognizer.ERROR_NO_MATCH ->
                        message = "찾을 수 없음"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
                        message = "RECOGNIZER 실행중"
                    SpeechRecognizer.ERROR_SERVER ->
                        message = "서버가 이상함"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                        message = "말하는 시간초과"
                    else ->
                        message = "알 수 없는 오류"
                }
                Toast.makeText(applicationContext, "에러 발생 $message", Toast.LENGTH_SHORT).show()
            }

            /* STT 값 배열에 저장하여 String으로 변환하기 */
            override fun onResults(results: Bundle?) {
                var matches: java.util.ArrayList<String> = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) as java.util.ArrayList<String>

                for (i in 0 until matches.size) {
                    tvResult = matches[i]
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }

    override fun onAddButtonClicked(todoContent: String) {
    }

    override fun onCancelButtonClicked() {
    }
}