package com.example.soundy

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

class DirectoryAdapter(val directoryList: ArrayList<Directorys>) : RecyclerView.Adapter<DirectoryAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryAdapter.CustomViewHolder {
        /* dir_list_item 화면 inflate */
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dir_list_item, parent, false)
        return CustomViewHolder(view)
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