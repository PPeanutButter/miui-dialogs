package com.peanut.sdk.miuidialog.content_wrapper

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import com.peanut.sdk.miuidialog.AddInFunction.resolveText
import com.peanut.sdk.miuidialog.AddInFunction.visible
import com.peanut.sdk.miuidialog.MIUIDialog
import com.peanut.sdk.miuidialog.WhichButton

typealias InputCallback = (charSequence:CharSequence?, dialog:MIUIDialog) -> Unit

/**
 * 保存输入框的入参
 */
class InputWrapper(
        private val hint: String? = null,
        private val hintRes: Int? = null,
        private val preFill: CharSequence? = null,
        private val preFillRes: Int? = null,
        private val inputType: Int = InputType.TYPE_CLASS_TEXT,
        private val maxLength: Int? = null,
        private val multiLines:Boolean = false,
        val waitForPositiveButton: Boolean = true,
        val allowEmpty: Boolean = false,
        val callback: InputCallback? = null
){
    /**
     * 处理输入框的显示与交互逻辑
     */
    fun populate(it: EditText,context: Context,miuiDialog: MIUIDialog){
        it.visible()
        if (maxLength!=null)
            Log.v("MIUI-Dialog","maxLength not supported yet")
        it.hint = context.resolveText(this.hint, this.hintRes)
        context.resolveText(this.preFill, this.preFillRes).let { prefill ->
            it.setText(prefill)
            if (this.allowEmpty.not())
                miuiDialog.setActionButtonEnabled(WhichButton.POSITIVE, prefill?.isNotEmpty() == true || prefill?.isNotBlank() == true)
        }
        it.inputType = this.inputType
        it.requestFocus()
        if (this.multiLines)
            it.inputType = it.inputType or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        it.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (this@InputWrapper.allowEmpty.not())
                    miuiDialog.setActionButtonEnabled(WhichButton.POSITIVE, s?.isNotEmpty() == true || s?.isNotBlank() == true)
                if (!this@InputWrapper.waitForPositiveButton)
                    this@InputWrapper.callback?.invoke(s, miuiDialog)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }
}