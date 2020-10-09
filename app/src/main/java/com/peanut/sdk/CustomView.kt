package com.peanut.sdk

import android.content.Context
import android.widget.Button
import com.peanut.sdk.miuidialog.AddInFunction.toast
import com.peanut.sdk.miuidialog.MIUIDialog

fun customView(context: Context) {
    MIUIDialog(context).show {
        title(text = "自定义View演示")
        customView(R.layout.example){
            it.findViewById<Button>(R.id.button).setOnClickListener {
                "you got me!".toast(context)
            }
        }
    }
}