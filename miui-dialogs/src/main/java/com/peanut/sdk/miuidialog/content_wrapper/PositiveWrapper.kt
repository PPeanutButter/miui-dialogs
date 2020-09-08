package com.peanut.sdk.miuidialog.content_wrapper

import com.peanut.sdk.miuidialog.MIUIDialog

typealias PositiveCallback = (MIUIDialog) -> Unit
/**
 * 保存Positive按钮入参
 */
class PositiveWrapper(
        val res: Int? = null,
        val text: CharSequence? = null,
        val countdown:Int? = null,
        val click: PositiveCallback? = null
)