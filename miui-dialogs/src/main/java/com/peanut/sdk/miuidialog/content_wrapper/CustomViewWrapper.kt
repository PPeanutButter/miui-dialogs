package com.peanut.sdk.miuidialog.content_wrapper

import android.view.View

typealias viewHandle = (view:View)->Unit
class CustomViewWrapper(
        val viewRes: Int? = null,
        val view: View? = null,
        val scrollable: Boolean = false,
        val noVerticalPadding: Boolean = false,
        val horizontalPadding: Boolean = false,
        val dialogWrapContent: Boolean = false,
        val func:viewHandle? = null
)