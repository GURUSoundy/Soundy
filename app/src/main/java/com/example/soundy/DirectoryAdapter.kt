package com.example.soundy

import android.content.Intent
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

class DirectoryAdapter(val directoryList: ArrayList<Directorys>) : RecyclerView.Adapter<DirectoryAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryAdapter.CustomViewHolder {
        /* dir_list_item 화면 inflate */
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dir_list_item, parent, false)
        return CustomViewHolder(view).apply {
            /* 아이템(디렉토리) 클릭 이벤트 리스너 */
            itemView.setOnClickListener {
                /* 클릭된 아이템의 포지션 */
                val curPos : Int = adapterPosition
                /* curPos에 위치한 아이템 가져오기 */
                val directory: Directorys = directoryList.get(curPos)

                /* parent.context->FileListActivity 클릭한 아이템(디렉토리)로 이동 */
                val intent = Intent(parent.context, FileListDetailActivity::class.java)
                intent.putExtra("dirName", directory.dirName)
                intent.run { parent.context.startActivity(intent) }
            }
        }
    }

    override fun getItemCount(): Int {
        return directoryList.size
    }

    override fun onBindViewHolder(holder: DirectoryAdapter.CustomViewHolder, position: Int) {
        holder.dirName.text = directoryList.get(position).dirName
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /* 디렉토리 이름 */
        val dirName = itemView.findViewById<TextView>(R.id.dirName)
    }
}