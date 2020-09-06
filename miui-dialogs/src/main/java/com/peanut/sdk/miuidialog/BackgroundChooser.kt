package com.peanut.sdk.miuidialog

object BackgroundChooser {
    fun getActionButtonDrawable(light:Boolean = true,miuiVersion:Int = MIUIVersion.MIUI11):Int{
        return when(miuiVersion){
            MIUIVersion.MIUI11-> if (light) R.drawable.botton_background else R.drawable.botton_background_dark
            else->0
        }
    }

    fun getInputFieldDrawable(light:Boolean = true,miuiVersion:Int = MIUIVersion.MIUI11):Int{
        return when(miuiVersion){
            MIUIVersion.MIUI11-> if (light) R.drawable.miui_input_bg else R.drawable.miui_input_bg_dark
            else->0
        }
    }
}