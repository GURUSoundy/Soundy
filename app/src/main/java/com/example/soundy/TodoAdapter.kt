package com.example.soundy

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(val todoList: ArrayList<Todos>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase
    lateinit var parentContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.TodoViewHolder {
        /* todo_list_item 화면 inflate */
        parentContext = parent.context
        val view = LayoutInflater.from(parentContext).inflate(R.layout.todo_list_item, parent, false)
        return TodoViewHolder(view)
        }

    override fun onBindViewHolder(holder: TodoAdapter.TodoViewHolder, position: Int) {
        var todoText = todoList.get(position).todoText
        holder.todoText.text = todoText

        /* DB에 저장된 todoChecked 값이 0이면 false, 1이면 true 상태로 출력 */
        if (todoList.get(position).todoChecked != 0) {
            holder.todoChecked.isChecked = true
            holder.todoText.paintFlags = holder.todoText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.todoChecked.isChecked = false
            holder.todoText.paintFlags = holder.todoText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        dbManager = DBManager(parentContext, "TodoList", null, 1)
        sqliteDB = dbManager.writableDatabase

        holder.todoChecked.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                sqliteDB.execSQL("UPDATE TodoList SET isChecked = 1 where list = '$todoText';")
                /* 완료한 경우 취소선 긋기 */
                holder.todoText.paintFlags = holder.todoText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                sqliteDB.execSQL("UPDATE TodoList SET isChecked = 0 where list = '$todoText';")
                holder.todoText.paintFlags = holder.todoText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /* 투두 이름, 완료 여부 */
        val todoChecked = itemView.findViewById<CheckBox>(R.id.todoChecked)
        val todoText = itemView.findViewById<TextView>(R.id.todoText)
    }
}
