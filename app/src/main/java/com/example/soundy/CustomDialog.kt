package com.example.soundy

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import java.util.*

class CustomDialog(context: Context, Interface: CustomDialogInterface) : Dialog(context) {
    var customDialogInterface: CustomDialogInterface = Interface

    lateinit var btnAdd: Button
    lateinit var btnCancel: Button
    lateinit var newDirectoryName: EditText
    lateinit var routineEndDate: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plus_directory_popup)

        btnAdd = findViewById(R.id.btnAdd)
        btnCancel = findViewById(R.id.btnCancle)
        routineEndDate = findViewById(R.id.endDate)
        var dateString = ""

        /* 배경을 투명하게 */
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        /* Date Picker (Calendar) */
        routineEndDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${year}/${month+1}/${dayOfMonth}"
                routineEndDate.setText(dateString)
            }
            DatePickerDialog(context, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // 추가 버튼 클릭 시 onAddButtonClicked 호출 후 종료
        btnAdd.setOnClickListener {
            newDirectoryName = findViewById(R.id.newTodoContent)
            routineEndDate = findViewById(R.id.endDate)

            Log.d("routine", routineEndDate.text.toString())

            var dirName = ""
            var endDate = ""
            if (newDirectoryName.text.toString() != "" && routineEndDate.text.toString() != "") {
                dirName = newDirectoryName.text.toString()
                endDate = routineEndDate.text.toString()
            }
            customDialogInterface.onAddButtonClicked(dirName, endDate)
            dismiss()
        }

        // 취소 버튼 클릭 시 onCancelButtonClicked 호출 후 종료
        btnCancel.setOnClickListener {
            customDialogInterface.onCancelButtonClicked()
            dismiss()
        }
    }
}