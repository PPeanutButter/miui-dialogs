package com.peanut.sdk

import android.content.Context
import com.peanut.sdk.miuidialog.AddInFunction.toast
import com.peanut.sdk.miuidialog.MIUIDialog
import com.peanut.sdk.miuidialog.WhichButton

fun miui11input(context: Context){
    MIUIDialog(context = context).show {
        title(text = "Use Google\'s Location Services?")
        input(hint = "Type something",waitForPositiveButton = false){ charSequence, _ ->
            charSequence?.toString()?.toast(context)
        }
        positiveButton(text = "Agree")
        negativeButton(text = "Disagree"){
            "You clicked negative button!".toast(context)
        }
    }
}

fun miui11InputWait(context: Context){
    MIUIDialog(context).show {
        title(text = "Use Google\'s Location Services?")
        input(hint = "Type something"){ charSequence, _ ->
            charSequence?.toString()?.toast(context)
        }
        positiveButton(text = "Agree")
        negativeButton(text = "Disagree"){
            "You clicked negative button!".toast(context)
        }
    }
}

fun miui11InputWaitMultilines(context: Context){
    MIUIDialog(context).show {
        title(text = "Use Google\'s Location Services?")
        input(hint = "Type something", multiLines = true) { charSequence, _ ->
            charSequence?.toString()?.toast(context)
        }
        positiveButton(text = "Agree")
        negativeButton(text = "Disagree") {
            "You clicked negative button!".toast(context)
        }
    }
}

fun customValidation(context: Context) {
    MIUIDialog(context).show {
        title(text = "输入以'a'开头的单词")
        input(waitForPositiveButton = false) { charSequence, dialog ->
            val isValid = charSequence?.startsWith("a", true)
            dialog.setInputError(text = if (isValid == true || charSequence.isNullOrEmpty()) null else "Must start with an 'a'!")
            dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid==true)
        }
        positiveButton(text = "Accept")
        negativeButton(text = "Cancel")
    }
}