package com.peanut.sdk

import android.content.Context
import com.peanut.sdk.miuidialog.MIUIDialog

fun icon(context: Context) {
    MIUIDialog(context = context).show {
        title(text = "显示标题图标")
        icon(R.mipmap.ic_launcher_round)
        message(text = "显示内容文字")
        positiveButton(text = "Accept")
        negativeButton(text = "Cancel")
    }
}

fun imageview(context: Context) {
    MIUIDialog(context = context).show {
        title(text = "显示标题图标")
        message(text = "显示内容文字")
        messageImg(R.mipmap.wechat_reword)
        positiveButton(text = "Accept")
        negativeButton(text = "Cancel")
    }
}