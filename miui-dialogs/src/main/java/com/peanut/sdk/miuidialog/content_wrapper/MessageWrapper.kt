package com.peanut.sdk.miuidialog.content_wrapper

typealias MessageSetting = MessageWrapper.() -> Unit
typealias HtmlClickCallback = (link: String) -> Unit
class MessageWrapper(
        val res: Int? = null,
        val text: CharSequence? = null,
        val messageSetting: MessageSetting? = null
){
    var htmlClickCallback:HtmlClickCallback?=null
    var isHtml:Boolean = false

    fun html(onLinkClick: HtmlClickCallback? = null): MessageWrapper {
        isHtml = true
        htmlClickCallback = onLinkClick
        return this
    }
}