package com.peanut.sdk.miuidialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.utils.MDUtil
import com.afollestad.materialdialogs.utils.MDUtil.resolveColor
import com.peanut.sdk.miuidialog.AddInFunction.resolveText
import com.peanut.sdk.miuidialog.AddInFunction.visible
import com.peanut.sdk.miuidialog.BackgroundChooser.getActionButtonDrawable
import com.peanut.sdk.miuidialog.BackgroundChooser.getInputFieldDrawable
import com.peanut.sdk.miuidialog.MIUIVersion.MIUI11
import com.peanut.sdk.miuidialog.content_wrapper.InputWrapper
import com.peanut.sdk.miuidialog.content_wrapper.MessageWrapper

/**
 * MIUI11计划：
 *              list、input与按钮倒计时
 */
class MIUIDialog(private val context: Context, private val miuiVersion: Int = MIUI11) {
    private var dialog: MaterialDialog? = null

    private var mTitle: String? = null

    private var positiveAction: PositiveCallback? = null
    private var positiveText: String? = null

    private var negativeAction: NegativeCallback? = null
    private var negativeText: String? = null

    private var dismissAction: DismissCallback? = null

    private var inputWrapper: InputWrapper? = null
    private var messageWrapper: MessageWrapper? = null

    private var miui_view:View?=null

    private var miui_light:Boolean = true

    fun title(@StringRes res: Int? = null, text: String? = null) {
        MDUtil.assertOneSet("title", text, res)
        this.mTitle = text?:res?.let { context.getString(it) }
    }

    /**
     * 还差支持HTML
     */
    fun message(@StringRes res: Int? = null, text: CharSequence? = null) {
        MDUtil.assertOneSet("title", text, res)
        messageWrapper = MessageWrapper(res, text)
    }

    fun positiveButton(text: String, click: PositiveCallback? = null) {
        this.positiveText = text
        this.positiveAction = click
    }

    fun negativeButton(text: String, click: PositiveCallback? = null) {
        this.negativeText = text
        this.negativeAction = click
    }

    fun show(func: MIUIDialog.() -> Unit): MIUIDialog = apply {
        func()
        this.show()
    }

    fun onDismiss(callback: DismissCallback): MIUIDialog {
        this.dismissAction = callback
        return this
    }

    fun input(
            hint: String? = null,
            @StringRes hintRes: Int? = null,
            prefill: CharSequence? = null,
            @StringRes prefillRes: Int? = null,
            inputType: Int = InputType.TYPE_CLASS_TEXT,
            maxLength: Int? = null,
            multiLines: Boolean = false,
            waitForPositiveButton: Boolean = true,
            allowEmpty: Boolean = false,
            callback: InputCallback? = null
    ): MIUIDialog {
        inputWrapper = InputWrapper(hint, hintRes, prefill, prefillRes, inputType, maxLength,multiLines, waitForPositiveButton, allowEmpty, callback)
        return this
    }

    fun getInputField() = miui_view?.findViewById<EditText>(R.id.miui_input)

    fun setActionButtonEnabled(
            which: WhichButton,
            enabled: Boolean
    ) {
        when(which){
            WhichButton.POSITIVE->{
                miui_view?.findViewById<Button>(R.id.miui_button_positive)?.let {
                    it.isEnabled = enabled
                    it.setTextColor(if (enabled) Color.parseColor("#0b94f2") else Color.GRAY)
                }
            }
            WhichButton.NEGATIVE->{
                miui_view?.findViewById<Button>(R.id.miui_button_negative)?.let {
                    it.isEnabled = enabled
                    it.setTextColor(if (enabled) ThemedColor.titleColor(miui_light) else Color.GRAY)
                }
            }
            else->{
                //miui不支持显示中立按钮
            }
        }
    }

    private fun cancel() = dialog?.cancel()

    @SuppressLint("InflateParams")
    fun show() {
        //处理不同的MIUI版本
        miui_view = when (miuiVersion) {
            MIUI11 -> LayoutInflater.from(context).inflate(R.layout.miui11layout, null)
            else -> null
        }
        //处理主题色
        miui_light = resolveColor(attr = R.attr.md_background_color, context = context) {
            resolveColor(attr = R.attr.colorBackgroundFloating, context = context)
        }.let {
            val r: Float = (it shr 16 and 0xff) / 255.0f
            val g: Float = (it shr 8 and 0xff) / 255.0f
            val b: Float = (it and 0xff) / 255.0f
            0.299 * r + 0.587 * g + 0.114 * b
        }.let {
            it > 0.5
        }
        miui_view?.let {
            //处理标题
            it.findViewById<TextView>(R.id.miui_title).let { textView ->
                textView.text = mTitle
                textView.setTextColor(ThemedColor.titleColor(miui_light))
            }
            //处理内容
            messageWrapper?.let {wrapper->
                it.findViewById<TextView>(R.id.miui_message).let { textView ->
                    textView.visible()
                    textView.text = context.resolveText(wrapper.text,wrapper.res)
                    textView.setTextColor(ThemedColor.titleColor(miui_light))
                }
            }
            //处理输入框
            inputWrapper?.let {wrapper->
                it.findViewById<EditText>(R.id.miui_input).let { input ->
                    input.visible()
                    input.setTextColor(ThemedColor.titleColor(miui_light))
                    input.hint = context.resolveText(wrapper.hint,wrapper.hintRes)
                    context.resolveText(wrapper.preFill,wrapper.preFillRes).let {prefill->
                        input.setText(prefill)
                        if (wrapper.allowEmpty.not())
                            this@MIUIDialog.setActionButtonEnabled(WhichButton.POSITIVE, prefill?.isNotEmpty() == true || prefill?.isNotBlank() == true)
                    }
                    input.inputType = wrapper.inputType
                    input.requestFocus()
                    if (wrapper.multiLines)
                        input.inputType = input.inputType or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                    input.background = ResourcesCompat.getDrawable(context.resources, getInputFieldDrawable(miui_light, miuiVersion),null)
                    input.addTextChangedListener(object : TextWatcher{
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            if (!wrapper.waitForPositiveButton)
                                wrapper.callback?.invoke(s,this@MIUIDialog)
                            if (wrapper.allowEmpty.not())
                                this@MIUIDialog.setActionButtonEnabled(WhichButton.POSITIVE, s?.isNotEmpty() == true || s?.isNotBlank() == true)
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }

                    })
                }
            }
            //positive 按钮
            if (positiveText != null) {
                it.findViewById<Button>(R.id.miui_button_positive).let { button ->
                    button.visible()
                    button.text = positiveText
                    button.background = ResourcesCompat.getDrawable(context.resources,getActionButtonDrawable(miui_light, miuiVersion),null)
                    button.setOnClickListener {
                        cancel()
                        positiveAction?.invoke(this)
                        if (inputWrapper?.waitForPositiveButton == true)
                            inputWrapper?.callback?.invoke(this.getInputField()?.text,this)
                    }
                }
                it.findViewById<LinearLayout>(R.id.miui_action_panel).visible()
            }
            //negative 按钮
            if (negativeText != null) {
                it.findViewById<Button>(R.id.miui_button_negative).let { button ->
                    button.visible()
                    button.text = negativeText
                    button.setTextColor(ThemedColor.titleColor(miui_light))
                    button.background = ResourcesCompat.getDrawable(context.resources,getActionButtonDrawable(miui_light, miuiVersion),null)
                    button.setOnClickListener {
                        cancel()
                        negativeAction?.invoke(this)
                    }
                }
                it.findViewById<LinearLayout>(R.id.miui_action_panel).visible()
            }
        }
        dialog = MaterialDialog(context, BottomSheet(layoutMode = LayoutMode.WRAP_CONTENT)).show {
            customView(view = miui_view, noVerticalPadding = true)
            onDismiss {
                dismissAction?.invoke(this@MIUIDialog)
            }
        }
    }
}

typealias PositiveCallback = (MIUIDialog) -> Unit
typealias NegativeCallback = (MIUIDialog) -> Unit
typealias DismissCallback = (MIUIDialog) -> Unit
typealias InputCallback = (CharSequence?,MIUIDialog) -> Unit