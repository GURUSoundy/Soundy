package com.example.soundy

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soundy.databinding.ActivityCalendarBinding
import kotlinx.android.synthetic.main.activity_calendar.rvTodoList
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarActivity : AppCompatActivity(), OnItemListner {

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase
    private lateinit var binding: ActivityCalendarBinding

    /* 년월 변수 */
    lateinit var selectedDate: LocalDate

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        overridePendingTransition(R.anim.horizon_enter, R.anim.none)

        /* 바인딩 초기화 */
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar)

        /* 뒤로가기 버튼 */
        binding.btnBack.setOnClickListener{
            Log.d("태그", "뒤로가기 버튼 클릭")
            finish()
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }

        /* 마이페이지 이동 기능 */
        binding.btnMypage.setOnClickListener {
            Log.d("태그", "마이페이지 버튼 클릭")
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        /* 현재 날짜 */
        selectedDate = LocalDate.now()

        /* 화면 설정 */
        setMonthView()

        /* 이전달 버튼 이벤트 */
        binding.preBtn.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            setMonthView()
        }

        /* 다음달 버튼 이벤트 */
        binding.nextBtn.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            setMonthView()
        }

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        var date: String = current.format(formatter)
        Log.d("날짜", date)

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

    /* 날짜 화면에 보여주기 */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {
        binding.yearMonthText.text = yearMonthFromDate(selectedDate)

        /* 날짜 생성해서 리스트에 담기 */
        val dayList = dayInMonthArray(selectedDate)

        /* 어댑터 초기화 */
        val adapter = CalendarAdapter(dayList, this)

        /* 레이아웃 설정 */
        var manager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)

        /* 레이아웃 적용 */
        binding.recyclerView.layoutManager = manager

        /* 어댑터 적용 */
        binding.recyclerView.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun yearMonthFromDate(date: LocalDate): String {
        var formatter = DateTimeFormatter.ofPattern("yyyy.MM")
        return date.format(formatter)
    }

    /* 날짜 생성 */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun dayInMonthArray(date: LocalDate): ArrayList<String> {
        var dayList = ArrayList<String>()

        var yearMonth = YearMonth.from(date)

        /* 해당 월 마지막 날짜 가져오기 */
        var lastDay = yearMonth.lengthOfMonth()

        /* 해당 월 첫 날짜 가져오기 */
        var firstDay = selectedDate.withDayOfMonth(1)

        /* 첫번째 날 요일 가져오기 */
        var dayOfWeek = firstDay.dayOfWeek.value

        for (i in 1..41) {
            if (i <= dayOfWeek || i > (lastDay + dayOfWeek)) {
                dayList.add("")
            } else {
                dayList.add((i - dayOfWeek).toString())
            }
        }
        return dayList
    }

    /* 아이템(날짜) 클릭 이벤트 리스너 */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(dayText: String) {
        var yearMonthDay = yearMonthFromDate(selectedDate) + "." + dayText
        // Toast.makeText(this, yearMonthDay, Toast.LENGTH_SHORT).show()

        val intent = Intent(this, ToDoListActivity::class.java)
        intent.putExtra("month", yearMonthFromDate(selectedDate))
        intent.putExtra("day", dayText)
        startActivity(intent)
    }

}

