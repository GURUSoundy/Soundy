package com.example.soundy

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.widget.CalendarView
import android.widget.GridLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soundy.databinding.ActivityTrycalBinding
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
    /* 년월 변수 */
    lateinit var selectedDate: LocalDate

    //lateinit var calendar: MaterialCalendarView
    //val TodayDecorator: MaterialCalendarView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trycal)

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




        /*
        calendar = view.findViewById(R.id.calendar)
        calendar.setSelectedDate(CalendarDay.today())

        calendar.addDecorator(TodayDecorator())

        calendar.addDecorator(TodayDecorator())
        class TodayDecorator: DayViewDecorator {
            //calendar.addDecorator(TodayDecorator)

            private var date = CalendarDay.today()

            override fun shouldDecorate(day: CalendarDay?): Boolean {
                return day?.equals(date)!!
            }

            override fun decorate(view: DayViewFacade?) {
                view?.addSpan(StyleSpan(Typeface.BOLD))
                view?.addSpan(RelativeSizeSpan(1.4f))
                view?.addSpan(ForegroundColorSpan(Color.parseColor("#1D872A")))
            }

        }
        //calendar.addDecorator(TodayDecorator)

        /*이벤트가 발생한 날 날짜 아래에 조그만 점을 찍어주는 Decorator 클래스*/
        class EventDecorator(dates: Collection<CalendarDay>): DayViewDecorator {

            var dates: HashSet<CalendarDay> = HashSet(dates)

            override fun shouldDecorate(day: CalendarDay?): Boolean {
                return dates.contains(day)
            }

            override fun decorate(view: DayViewFacade?) {
                view?.addSpan(DotSpan(5F, Color.parseColor("#1D872A")))
            }
        }
        /*해당 이벤트는 일단 날짜가 선택되면 발생하도록 설정*/
        calendar.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                calendar.addDecorator(EventDecorator(Collections.singleton(date)))
            }
        }) */
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
        var formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
        return date.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun yearMonthFromDate(date: LocalDate): String {
        var formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
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
        var yearMonthDay = yearMonthFromDate(selectedDate) + " " + dayText + "일"
        Toast.makeText(this, yearMonthDay, Toast.LENGTH_SHORT).show()
    }

}

