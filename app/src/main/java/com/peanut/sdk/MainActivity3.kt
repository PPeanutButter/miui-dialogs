package com.peanut.sdk

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
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

    fun miui11Html(view:View){
        MIUIDialog(this).show {
            title(text = "Use Google\'s Location Services?")
            message(text = "Let Google help apps determine location. <a href='baidu.com'>This means sending anonymous location data to Google</a>, even when no apps are running."){
                html()
            }
        }
    }

    fun miui11HtmlCallback(view:View){
        MIUIDialog(this).show {
            title(text = "Use Google\'s Location Services?")
            message(text = "Let Google help apps determine location. <a href='baidu.com'>This means sending anonymous location data to Google</a>, even when no apps are running."){
                html{
                    it.toast(this@MainActivity3)
                }
            }
        }
    }

    fun test(){
        MaterialDialog(this).show {
            onDismiss {
            }
            message(){
                html() {  }

            }
//            setActionButtonEnabled()
            positiveButton()
            input()
            title()
            cancel()
            getInputField()
        }

        MIUIDialog(this).show {
            cancel()
            dismiss()
            getInputField()
        }
    }

    fun miui11ActionButton(view:View){
        MIUIDialog(this).show {
            title(text = "Use Google\'s Location Services?")
            message(text = "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
            positiveButton(text = "Agree"){
                "you clicked positive button!".toast(this@MainActivity3)
            }
//            negativeButton(text = "Disagree"){
//                "you clicked negative button!".toast(this@MainActivity3)
//            }
        }
    }

    fun miui11PositiveCountdown(view:View){
        MIUIDialog(this).show {
            title(text = "Use Google\'s Location Services?")
            message(text = "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
            positiveButton(text = "Agree",countdown = 5){
                "you clicked positive button!".toast(this@MainActivity3)
            }
            negativeButton(text = "Disagree"){
                "you clicked negative button!".toast(this@MainActivity3)
            }
        }
    }

    fun miui11NegativeCountdown(view:View){
        MIUIDialog(this).show {
            title(text = "Use Google\'s Location Services?")
            message(text = "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
            positiveButton(text = "Agree"){
                "you clicked positive button!".toast(this@MainActivity3)
            }
            negativeButton(text = "Disagree",countdown = 10){
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
            input(hint = "Type something", multiLines = true) { charSequence, _ ->
                charSequence?.toString()?.toast(this@MainActivity3)
            }
            positiveButton(text = "Agree")
            negativeButton(text = "Disagree") {
                "You clicked negative button!".toast(this@MainActivity3)
            }
        }
    }

    fun custom_validation(view: View) {
        MIUIDialog(this).show {
            input(waitForPositiveButton = false) { charSequence, dialog ->
                val isValid = charSequence?.startsWith("a", true)
                dialog.setInputError(text = if (isValid == true || charSequence.isNullOrEmpty()) null else "Must start with an 'a'!")
                dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid==true)
            }
            positiveButton(text = "Accept")
        }
    }
}