package com.peanut.sdk.miuidialog

import android.graphics.Color

/**
 * 一些颜色适配器
 */
object ThemedColor {
    /**
     * 强调色
     */
    fun mainColor(light:Boolean = true) = Color.parseColor(if (light) "#000000" else "#ffffff" )
}