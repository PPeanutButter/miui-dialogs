package com.peanut.sdk

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.peanut.sdk.miuidialog.AddInFunction.toast
import com.peanut.sdk.miuidialog.MIUIDialog

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
    }

    fun miui11Basic(view:View){
        MIUIDialog(this).show {
            title(text = "Use Google\'s Location Services?")
            message(text = "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
        }
    }

    fun test(){
        MaterialDialog(this).show {
            onDismiss {
            }
            message()
//            input()
            title()
        }
    }

    fun miui11ActionButton(view:View){
        MIUIDialog(this).show {
            title(text = "Use Google\'s Location Services?")
            message(text = "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
            positiveButton(text = "Agree"){
                "you clicked positive button!".toast(this@MainActivity3)
            }
            negativeButton(text = "Disagree"){
                "you clicked negative button!".toast(this@MainActivity3)
            }
        }
    }

    fun miui11Callbacks(view:View){
        MIUIDialog(this).show {
            title(text = "Use Google\'s Location Services?")
            message(text = "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
            positiveButton(text = "Agree"){
                "You clicked positive button!".toast(this@MainActivity3)
            }
            negativeButton(text = "Disagree"){
                "You clicked negative button!".toast(this@MainActivity3)
            }
            onDismiss {
                "You canceled MIUI-Dialog!".toast(this@MainActivity3)
            }
        }
    }

    fun miui11input(view: View){
        MIUIDialog(this).show {
            title(text = "Use Google\'s Location Services?")
            input(hint = "Type something",waitForPositiveButton = false){ charSequence, _ ->
                charSequence?.toString()?.toast(this@MainActivity3)
            }
            positiveButton(text = "Agree")
            negativeButton(text = "Disagree"){
                "You clicked negative button!".toast(this@MainActivity3)
            }
        }
    }

    fun miui11InputWait(view: View){
        MIUIDialog(this).show {
            title(text = "Use Google\'s Location Services?")
            input(hint = "Type something"){ charSequence, _ ->
                charSequence?.toString()?.toast(this@MainActivity3)
            }
            positiveButton(text = "Agree")
            negativeButton(text = "Disagree"){
                "You clicked negative button!".toast(this@MainActivity3)
            }
        }
    }

    fun miui11InputWaitMultilines(view: View){
        MIUIDialog(this).show {
            title(text = "Use Google\'s Location Services?")
            input(hint = "Type something", multiLines = true){ charSequence, _ ->
                charSequence?.toString()?.toast(this@MainActivity3)
            }
            positiveButton(text = "Agree")
            negativeButton(text = "Disagree"){
                "You clicked negative button!".toast(this@MainActivity3)
            }
        }
    }
}