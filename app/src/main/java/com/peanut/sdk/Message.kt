package com.peanut.sdk

import android.content.Context
import com.peanut.sdk.miuidialog.AddInFunction.toast
import com.peanut.sdk.miuidialog.MIUIDialog

fun customizingTheMessage(context: Context) {
    MIUIDialog(context = context).show {
        title(text = "Use Google\'s Location Services?")
        message(text = "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.") {
            html()
            lineSpacing(1.0f)
        }
        positiveButton(text = "Agree") {
            "you clicked positive button!".toast(context)
        }
    }
}