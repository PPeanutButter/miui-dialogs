package com.peanut.sdk.miuidialog.content_wrapper

import android.content.Context
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.peanut.sdk.miuidialog.AddInFunction.resolveText
import com.peanut.sdk.miuidialog.AddInFunction.visible
import com.peanut.sdk.miuidialog.internal.LinkTransformationMethod

typealias MessageSetting = MessageWrapper.MessageSettings.() -> Unit
typealias HtmlClickCallback = (link: String) -> Unit

/**
 * 保存内容入参
 */
class MessageWrapper(
        val res: Int? = null,
        val text: CharSequence? = null,
        val messageSetting: MessageSetting? = null
){
    var htmlClickCallback:HtmlClickCallback?=null
    var isHtml:Boolean = false
    var lineSpacingValue:Float = 1.0f

    /**
     * 处理内容消息框的显示与交互逻辑
     */
    fun populate(it:TextView,context: Context){
        it.visible()
        it.text = if (this.isHtml) HtmlCompat.fromHtml(context.resolveText(this.text, this.res)
                ?: "resolveText error", HtmlCompat.FROM_HTML_MODE_LEGACY) else context.resolveText(this.text, this.res)
        if (this.messageSetting != null) {
            this.htmlClickCallback?.let { htmlClickCallback ->
                it.transformationMethod = LinkTransformationMethod(htmlClickCallback)
            }
            it.movementMethod = LinkMovementMethod.getInstance()
            it.setLineSpacing(0f, lineSpacingValue)
        }
    }

    /**
     * 内容消息的额外设置
     */
    inner class MessageSettings{

        /**
         * 显示HTML并重写链接<a href=''/>的点击事件
         */
        fun html(onLinkClick: HtmlClickCallback? = null): MessageWrapper.MessageSettings = apply {
            isHtml = true
            htmlClickCallback = onLinkClick
        }

        /**
         * 设置行间距
         */
        fun lineSpacing(multiplier: Float): MessageWrapper.MessageSettings = apply {
            lineSpacingValue = multiplier
        }
    }
}

