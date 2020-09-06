package com.peanut.sdk.miuidialog

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.Toast

object AddInFunction {
    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun String.toast(context: Context,duration:Int = Toast.LENGTH_SHORT){
        Handler(context.mainLooper).post{
            Toast.makeText(context,this,duration).show()
        }
    }

    fun Context.resolveText(text:CharSequence?,res:Int?):String? = (text?.toString())?:res?.let { this.getString(it) }
    fun Context.resolveText(text:String?,res:Int?):String? = text?:res?.let { this.getString(it) }
}