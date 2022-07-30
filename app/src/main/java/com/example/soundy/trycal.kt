package com.example.soundy

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.widget.CalendarView
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import kotlinx.android.synthetic.main.activity_save_file.*
import kotlinx.android.synthetic.main.activity_trycal.*
import java.util.*

class trycal : AppCompatActivity() {

    lateinit var calendar: MaterialCalendarView
    //val TodayDecorator: MaterialCalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trycal)

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
        })
    }

}

