package com.peanut.sdk

import android.content.Context
import com.peanut.sdk.miuidialog.AddInFunction.toast
import com.peanut.sdk.miuidialog.MIUIDialog

fun miui11Html(context: Context) {
    MIUIDialog(context).show {
        title(text = "Use Google\'s Location Services?")
        message(text = "Let Google help apps determine location. <a href='baidu.com'>This means sending anonymous location data to Google</a>, even when no apps are running.") {
            html()
        }
    }
}

fun miui11HtmlCallback(context: Context){
    MIUIDialog(context).show {
        title(text = "Use Google\'s Location Services?")
        message(text = "Let Google help apps determine location. <a href='baidu.com'>This means sending anonymous location data to Google</a>, even when no apps are running."){
            html{
                it.toast(context)
            }
        }
    }
}