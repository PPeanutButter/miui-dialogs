package com.peanut.sdk

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.peanut.sdk.miuidialog.MIUIDialog
import com.peanut.sdk.miuidialog.WhichButton
import kotlinx.android.synthetic.main.activity_main3.*
import kotlinx.android.synthetic.main.activity_main3.progress

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
    }

    fun onButtonClick(view: View) {
        showDialog(when (view) {
            b_basic -> ::basic
            b_action -> ::miui11ActionButton
            b_callback -> ::miui11Callbacks
            b_action_countdown_p -> ::miui11PositiveCountdown
            b_action_countdown_n -> ::miui11NegativeCountdown
            b_icon -> ::icon
            b_imageview -> ::imageview
            b_line -> ::customizingTheMessage
            b_html -> ::miui11Html
            b_html_callback -> ::miui11HtmlCallback
            b_input -> ::miui11input
            b_input_wait -> ::miui11InputWait
            b_input_muti -> ::miui11InputWaitMultilines
            b_input_valid -> ::customValidation
            progress -> ::basicProgress
            else -> null
        })
    }

    private fun showDialog(func: ((context: Context) -> Unit)?) {
        func?.invoke(this)
    }

    fun test() {
        MaterialDialog(this).show {
            message() {
                html() { }
//                lineSpacing()
            }
//            setActionButtonEnabled()
            positiveButton()
            input()
            title()
            icon()
            cancel()
            cancelable(true)
        }

        MIUIDialog(this).show {
            cancel()
            dismiss()
            input()
            getInputField()
            getMessageTextView()
            cancelable = false
            setActionButtonEnabled(WhichButton.POSITIVE,true)
            onCancel { }
            onDismiss { }
            onShow { }
            onPreShow { }
        }
    }


}