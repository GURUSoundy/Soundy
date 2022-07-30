package com.example.soundy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.sql.Date
import java.text.SimpleDateFormat


class rvfileAdapter(var records: ArrayList<audioRecord>, var listener: OnItemClickListener): RecyclerView.Adapter<rvfileAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener{
        var rvFilename: TextView =itemView.findViewById(R.id.rvFilename)
        var rvMeta: TextView =itemView.findViewById(R.id.rvMeta)
        var checkbox: TextView =itemView.findViewById(R.id.rvFilename)

        init{
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        /*녹음파일 simpleclick*/
        override fun onClick(p0: View?) {
            val position=adapterPosition
            if(position != RecyclerView.NO_POSITION)
                listener.onItemClickListener(position)
        }
        /*녹음파일 꾹 누름 = longclick*/
        override fun onLongClick(p0: View?): Boolean {
            val position=adapterPosition
            if(position != RecyclerView.NO_POSITION)
                listener.onItemLongClickListener(position)
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rvfilelist,parent,false)  //rvfilelist
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return records.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position != RecyclerView.NO_POSITION){
            var record = records[position]

            var sdf = SimpleDateFormat("yyyy/MM/dd/")
            var date = Date (record.timestamp)
            var strDate = sdf.format(date)

            /*녹음파일에 보여질 정보(제목, 녹음길이)*/
            holder.rvFilename.text = record.filename
            holder.rvMeta.text = "${record.duration} $strDate"
        }
    }

}