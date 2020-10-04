package com.peanut.sdk

import android.content.Context
import com.peanut.sdk.miuidialog.MIUIDialog

fun basic(context: Context) {
    MIUIDialog(context).show {
        title(text = "Use Google\'s Location Services?")
        message(text = "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
    }
}