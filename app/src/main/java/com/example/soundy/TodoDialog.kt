package com.example.soundy

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class TodoDialog(context: Context, Interface: TodoDialogInterface) : Dialog(context) {
    var TodoDialogInterface: TodoDialogInterface = Interface

    lateinit var btnAdd: Button
    lateinit var btnCancel: Button
    lateinit var newTodoContent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plus_todo_popup)

        btnAdd = findViewById(R.id.btnAdd)
        btnCancel = findViewById(R.id.btnCancle)

        /* 배경을 투명하게 */
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 추가 버튼 클릭 시 onAddButtonClicked 호출 후 종료
        btnAdd.setOnClickListener {
            newTodoContent = findViewById(R.id.newTodoContent)

            var todo = ""
            if (newTodoContent.text.toString() != "") {
                todo = newTodoContent.text.toString()
            }
            TodoDialogInterface.onAddButtonClicked(todo)
            dismiss()
        }

        // 취소 버튼 클릭 시 onCancelButtonClicked 호출 후 종료
        btnCancel.setOnClickListener {
            TodoDialogInterface.onCancelButtonClicked()
            dismiss()
        }
    }
}