package com.peanut.sdk.miuidialog

import android.graphics.Color

object ThemedColor {
    fun titleColor(light:Boolean = true) = Color.parseColor(if (light) "#000000" else "#ffffff" )
    fun mesageColor(light:Boolean = true) = Color.parseColor(if (light) "#4a4a4a" else "#ffffff" )
}