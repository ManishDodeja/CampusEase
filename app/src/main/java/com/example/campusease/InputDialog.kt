package com.example.campusease
import android.app.Dialog
import android.content.Context
import android.widget.EditText

class InputDialog(context: Context): Dialog(context) {

    init{
        setContentView(R.layout.dialog_input)
    }

    fun getInputValue():String?{
        val txtInput:EditText=findViewById(R.id.PopUpEditText)
        return txtInput.text.toString()
    }
}