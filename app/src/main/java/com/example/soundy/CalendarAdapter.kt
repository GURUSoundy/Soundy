package com.example.soundy

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CalendarAdapter(val dayList: ArrayList<String>, val onItemListner: OnItemListner) :
    RecyclerView.Adapter<CalendarAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarAdapter.ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_item, parent, false)

        return ItemViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarAdapter.ItemViewHolder, position: Int) {
        holder.dayText.text = dayList.get(position)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        var date: String = current.format(formatter)

        /* 텍스트 색상 지정(토, 일) */
        if ((position + 1) % 7 == 0) {
            holder.dayText.setTextColor(Color.parseColor("#54BAB9"))
        } else if (position == 0 || position % 7 == 0) {
            holder.dayText.setTextColor(Color.parseColor("#F46B6B"))
        }

        /* 날짜 클릭 이벤트 */
        holder.itemView.setOnClickListener{
            /* 인터페이스를 통해 날짜 넘겨줌 */
            onItemListner.onItemClick(holder.dayText.text.toString())
        }
    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayText: TextView = itemView.findViewById(R.id.dayText)
    }

}