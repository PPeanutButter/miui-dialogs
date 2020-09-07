package com.peanut.sdk.miuidialog.content_wrapper

import com.peanut.sdk.miuidialog.MIUIDialog

typealias NegativeCallback = (MIUIDialog) -> Unit
class NegativeWrapper(
        val res: Int? = null,
        val text: CharSequence? = null,
        val click: NegativeCallback? = null
)