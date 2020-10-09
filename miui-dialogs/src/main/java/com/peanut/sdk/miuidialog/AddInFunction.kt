package com.peanut.sdk.miuidialog

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.res.ResourcesCompat

/**
 * 一些封装的方法
 */
object AddInFunction {
    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun View.gone() {
        this.visibility = View.GONE
    }

    fun String.toast(context: Context, duration: Int = Toast.LENGTH_SHORT) {
        Handler(context.mainLooper).post {
            Toast.makeText(context, this, duration).show()
        }
    }

    fun Int.createView(context: Context): View =
            LayoutInflater.from(context).inflate(this, null)

    fun Int.createDrawable(context: Context) = ResourcesCompat.getDrawable(context.resources,this,null)

    fun Context.resolveLayout(day: Boolean = true, @LayoutRes dayLayoutRes: Int, @LayoutRes nightLayoutRes: Int):View =
            if (day) dayLayoutRes.createView(this) else nightLayoutRes.createView(this)

    fun Context.resolveText(text: CharSequence?, res: Int?): String? = (text?.toString())
            ?: res?.let { this.getString(it) }

    fun Context.resolveText(text: String?, res: Int?): String? = text
            ?: res?.let { this.getString(it) }
}