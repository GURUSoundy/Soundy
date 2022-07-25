package com.example.soundy

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button

class CustomDialog(context: Context, Interface: CustomDialogInterface) : Dialog(context) {
    var customDialogInterface: CustomDialogInterface = Interface

    lateinit var btnAdd: Button
    lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plus_directory_popup)

        btnAdd = findViewById(R.id.btnAdd)
        btnCancel = findViewById(R.id.btnCancle)

        // 배경을 투명하게함
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 추가 버튼 클릭 시 onAddButtonClicked 호출 후 종료
        btnAdd.setOnClickListener {
            customDialogInterface.onAddButtonClicked()
            dismiss()
        }

        // 취소 버튼 클릭 시 onCancelButtonClicked 호출 후 종료
        btnCancel.setOnClickListener {
            customDialogInterface.onCancelButtonClicked()
            dismiss()
        }
    }
}