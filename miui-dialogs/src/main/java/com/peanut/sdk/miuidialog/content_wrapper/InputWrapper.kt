package com.peanut.sdk.miuidialog.content_wrapper

import android.text.InputType
import com.peanut.sdk.miuidialog.InputCallback

class InputWrapper(val hint: String? = null, val hintRes: Int? = null, val preFill: CharSequence? = null, val preFillRes: Int? = null, val inputType: Int = InputType.TYPE_CLASS_TEXT, val maxLength: Int? = null, val multiLines:Boolean = false, val waitForPositiveButton: Boolean = true, val allowEmpty: Boolean = false, val callback: InputCallback? = null)