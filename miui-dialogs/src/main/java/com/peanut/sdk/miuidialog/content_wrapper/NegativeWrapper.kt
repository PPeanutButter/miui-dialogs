package com.peanut.sdk.miuidialog.content_wrapper

import com.peanut.sdk.miuidialog.MIUIDialog

typealias NegativeCallback = (MIUIDialog) -> Unit

/**
 * 保存Negative按钮入参
 */
class NegativeWrapper(
        val res: Int? = null,
        val text: CharSequence? = null,
        val countdown:Int? = null,
        val click: NegativeCallback? = null
)