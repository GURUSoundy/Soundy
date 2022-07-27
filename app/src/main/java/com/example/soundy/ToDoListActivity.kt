package com.example.soundy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_trycal.*

class ToDoListActivity : AppCompatActivity() {

    //var userID: String = "userID"
    //lateinit var fname: String
    //lateinit var str: String
    //lateinit var updateBtn: Button
    //lateinit var deleteBtn: Button
    //lateinit var saveBtn: Button
    lateinit var diaryTextView: TextView
    lateinit var diaryContent: TextView
    lateinit var titleText: TextView
    //lateinit var contextEditText: EditText

    lateinit var btnBack: ImageButton
    lateinit var btnMypage: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

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

//        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
//            diaryTextView.visibility = View.VISIBLE
//            //saveBtn.visibility = View.VISIBLE
//            //contextEditText.visibility = View.VISIBLE
//            //diaryContent.visibility = View.INVISIBLE
//            //updateBtn.visibility = View.INVISIBLE
//            //deleteBtn.visibility = View.INVISIBLE
//            diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
//            //contextEditText.setText("")
//            //checkDay(year, month, dayOfMonth, userID)
//        }



    }
}