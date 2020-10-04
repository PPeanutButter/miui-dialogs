package com.peanut.sdk

import android.content.Context
import com.peanut.sdk.miuidialog.AddInFunction.toast
import com.peanut.sdk.miuidialog.MIUIDialog

fun miui11ActionButton(context: Context){
    MIUIDialog(context).show {
        title(text = "Use Google\'s Location Services?")
        message(text = "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
        positiveButton(text = "Agree"){
            "you clicked positive button!".toast(context)
        }
        negativeButton(text = "Disagree"){
            "you clicked negative button!".toast(context)
        }
    }
}

fun miui11PositiveCountdown(context: Context){
    MIUIDialog(context).show {
        title(text = "Use Google\'s Location Services?")
        message(text = "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
        positiveButton(text = "Agree",countdown = 5){
            "you clicked positive button!".toast(context)
        }
        negativeButton(text = "Disagree"){
            "you clicked negative button!".toast(context)
        }
    }
}

fun miui11NegativeCountdown(context: Context){
    MIUIDialog(context).show {
        title(text = "Use Google\'s Location Services?")
        message(text = "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
        positiveButton(text = "Agree"){
            "you clicked positive button!".toast(context)
        }
        negativeButton(text = "Disagree",countdown = 10){
            "you clicked negative button!".toast(context)
        }
    }
}

fun miui11Callbacks(context: Context){
    MIUIDialog(context).show {
        title(text = "Use Google\'s Location Services?")
        message(text = "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
        positiveButton(text = "Agree"){
            "You clicked positive button!".toast(context)
        }
        negativeButton(text = "Disagree"){
            "You clicked negative button!".toast(context)
        }
        onDismiss {
            "You canceled MIUI-Dialog!".toast(context)
        }
    }
}