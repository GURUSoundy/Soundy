package com.example.soundy

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.widget.CalendarView
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soundy.databinding.ActivityTrycalBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import kotlinx.android.synthetic.main.activity_save_file.*
import kotlinx.android.synthetic.main.activity_trycal.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class TryCalActivity : AppCompatActivity(), OnItemListner {

    private lateinit var binding: ActivityTrycalBinding
    lateinit var btnMypage: ImageButton

    /* 년월 변수 */
    lateinit var selectedDate: LocalDate

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trycal)

        /* 마이페이지 이동 기능 */
        btnMypage=findViewById<ImageButton>(R.id.btnMypage)
        btnMypage.setOnClickListener {
            val intent = Intent(this, mypage::class.java)
            startActivity(intent)
        }

        /* 바인딩 초기화 */
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trycal)

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
    }

    /* 날짜 화면에 보여주기 */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {
        binding.monthYearText.text = monthYearFromDate(selectedDate)

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
    private fun monthYearFromDate(date: LocalDate): String {
        var formatter = DateTimeFormatter.ofPattern("yyyy.MM")
        return date.format(formatter)
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
        Toast.makeText(this, yearMonthDay, Toast.LENGTH_SHORT).show()

        val intent = Intent(this, ToDoListActivity::class.java)
        intent.putExtra("month", yearMonthFromDate(selectedDate))
        intent.putExtra("day", dayText)
        startActivity(intent)
    }

}

