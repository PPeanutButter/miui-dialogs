package com.peanut.sdk.miuidialog.content_wrapper

import com.peanut.sdk.miuidialog.MIUIDialog

typealias PositiveCallback = (MIUIDialog) -> Unit
class PositiveWrapper(
        val res: Int? = null,
        val text: CharSequence? = null,
        val click: PositiveCallback? = null
)