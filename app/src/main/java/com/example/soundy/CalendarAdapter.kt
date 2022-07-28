package com.example.soundy

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(val dayList: ArrayList<String>, val onItemListner: OnItemListner) :
    RecyclerView.Adapter<CalendarAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarAdapter.ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_item, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarAdapter.ItemViewHolder, position: Int) {
        holder.dayText.text = dayList.get(position)

        /* 텍스트 색상 지정(토, 일) */
        if ((position + 1) % 7 == 0) {
            holder.dayText.setTextColor(Color.BLUE)
        } else if (position == 0 || position % 7 == 0) {
            holder.dayText.setTextColor(Color.RED)
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