package com.peanut.sdk.miuidialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.utils.MDUtil
import com.afollestad.materialdialogs.utils.MDUtil.resolveColor
import com.peanut.sdk.miuidialog.AddInFunction.gone
import com.peanut.sdk.miuidialog.AddInFunction.resolveLayout
import com.peanut.sdk.miuidialog.AddInFunction.resolveText
import com.peanut.sdk.miuidialog.AddInFunction.visible
import com.peanut.sdk.miuidialog.MIUIVersion.MIUI11
import com.peanut.sdk.miuidialog.content_wrapper.*
import com.peanut.sdk.miuidialog.internal.LinkTransformationMethod

/**
 * MIUI11计划：
 *              list、input与按钮倒计时
 */
class MIUIDialog(private val context: Context, private val miuiVersion: Int = MIUI11) {
    private var dialog: MaterialDialog? = null

    private var dismissAction: DismissCallback? = null

    private var titleWrapper: TitleWrapper? = null
    private var inputWrapper: InputWrapper? = null
    private var messageWrapper: MessageWrapper? = null
    private var positiveWrapper: PositiveWrapper? = null
    private var negativeWrapper: NegativeWrapper? = null

    private var miui_view: View? = null

    private var miui_light: Boolean = true

    /**
     * Shows a title, or header, at the top of the dialog.
     *
     * @param res The string resource to display as the title.
     * @param text The literal string to display as the title.
     */
    fun title(@StringRes res: Int? = null, text: String? = null): MIUIDialog = apply {
        MDUtil.assertOneSet("title", text, res)
        this.titleWrapper = TitleWrapper(res, text)
    }

    /**
     * Shows a message, below the title, and above the action buttons (and checkbox prompt).
     *
     * @param res The string resource to display as the message.
     * @param text The literal string to display as the message.
     */
    fun message(@StringRes res: Int? = null, text: CharSequence? = null, messageSetting: MessageSetting? = null): MIUIDialog = apply {
        MDUtil.assertOneSet("title", text, res)
        messageWrapper = MessageWrapper(res, text, messageSetting).apply {
            messageSetting?.invoke(this)
        }
    }

    /**
     * Shows a positive action button, in the far right at the bottom of the dialog.
     *
     * @param res The string resource to display on the title.
     * @param text The literal string to display on the button.
     * @param click A listener to invoke when the button is pressed.
     */
    fun positiveButton(@StringRes res: Int? = null, text: CharSequence? = null, click: PositiveCallback? = null): MIUIDialog = apply {
        this.positiveWrapper = PositiveWrapper(res, text, click)
    }

    /**
     * Shows a negative action button, to the left of the positive action button (or at the far
     * right if there is no positive action button).
     *
     * @param res The string resource to display on the title.
     * @param text The literal string to display on the button.
     * @param click A listener to invoke when the button is pressed.
     */
    fun negativeButton(@StringRes res: Int? = null, text: CharSequence? = null, click: NegativeCallback? = null): MIUIDialog = apply {
        this.negativeWrapper = NegativeWrapper(res, text, click)
    }

    fun show(func: MIUIDialog.() -> Unit): MIUIDialog = apply {
        func()
        this.show()
    }

    fun onDismiss(callback: DismissCallback): MIUIDialog = apply {
        this.dismissAction = callback
    }

    /**
     * Shows an input field as the content of the dialog. Can be used with a message and checkbox
     * prompt, but cannot be used with a list.
     *
     * @param hint The literal string to display as the input field hint.
     * @param hintRes The string resource to display as the input field hint.
     * @param prefill The literal string to pre-fill the input field with.
     * @param prefillRes The string resource to pre-fill the input field with.
     * @param inputType The input type for the input field, e.g. phone or email. Defaults to plain text.
     * @param maxLength The max length for the input field, shows a counter and disables the positive
     *    action button if the input length surpasses it.(not available yet!)
     * @param waitForPositiveButton When true, the [callback] isn't invoked until the positive button
     *    is clicked. Otherwise, it's invoked every time the input text changes. Defaults to true if
     *    the dialog has buttons.
     * @param allowEmpty Defaults to false. When false, the positive action button is disabled unless
     *    the input field is not empty.
     * @param callback A listener to invoke for input text notifications.
     */
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
    ): MIUIDialog = apply {
        inputWrapper = InputWrapper(hint, hintRes, prefill, prefillRes, inputType, maxLength, multiLines, waitForPositiveButton, allowEmpty, callback)
    }

    fun setActionButtonEnabled(
            which: WhichButton,
            enabled: Boolean
    ) {
        when(which){
            WhichButton.POSITIVE -> {
                miui_view?.findViewById<Button>(R.id.miui_button_positive)?.let {
                    it.isEnabled = enabled
                    it.setTextColor(if (enabled) Color.parseColor("#0b94f2") else Color.GRAY)
                }
            }
            WhichButton.NEGATIVE -> {
                miui_view?.findViewById<Button>(R.id.miui_button_negative)?.let {
                    it.isEnabled = enabled
                    it.setTextColor(if (enabled) ThemedColor.titleColor(miui_light) else Color.GRAY)
                }
            }
            else -> {
                //miui不支持显示中立按钮
            }
        }
    }

    fun cancel() = dialog?.cancel()

    fun getInputField() = miui_view?.findViewById<EditText>(R.id.miui_input)

    private fun calculateVisionLight() {
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
    }

    @SuppressLint("InflateParams")
    fun show() {
        //处理不同的MIUI版本
        miui_view = when (miuiVersion) {
            MIUI11 -> context.resolveLayout(miui_light, dayLayoutRes = R.layout.miui11layout, nightLayoutRes = R.layout.miui11layout_night)
            else -> null
        }
        calculateVisionLight()
        miui_view?.let {
            populateTitle(it)
            populateMessage(it)
            populateInput(it)
            populatePositiveButton(it)
            populateNegativeButton(it)
        }
        dialog = MaterialDialog(context, BottomSheet(layoutMode = LayoutMode.WRAP_CONTENT)).show {
            customView(view = miui_view, noVerticalPadding = true)
            onDismiss {
                dismissAction?.invoke(this@MIUIDialog)
            }
        }
    }

    private fun populateTitle(view: View) {
        if (titleWrapper != null) {
            view.findViewById<TextView>(R.id.miui_title).let {
                it.gone()
                titleWrapper?.let { wrapper ->
                    it.text = context.resolveText(res = wrapper.res, text = wrapper.text)
                    it.visible()
                }
            }
        }
    }

    private fun populateMessage(view: View) {
        view.findViewById<TextView>(R.id.miui_message).let {
            it.gone()
            messageWrapper?.let { wrapper ->
                it.visible()
                it.text = if (wrapper.isHtml) HtmlCompat.fromHtml(context.resolveText(wrapper.text, wrapper.res)
                        ?: "resolveText error", HtmlCompat.FROM_HTML_MODE_LEGACY) else context.resolveText(wrapper.text, wrapper.res)
                if (wrapper.messageSetting != null) {
                    wrapper.htmlClickCallback?.let { htmlClickCallback ->
                        it.transformationMethod = LinkTransformationMethod(htmlClickCallback)
                    }
                    it.movementMethod = LinkMovementMethod.getInstance()
                }
            }
        }
    }

    private fun populateInput(view: View) {
        view.findViewById<EditText>(R.id.miui_input).let {
            it.gone()
            inputWrapper?.let { wrapper ->
                it.visible()
                it.hint = context.resolveText(wrapper.hint, wrapper.hintRes)
                context.resolveText(wrapper.preFill, wrapper.preFillRes).let { prefill ->
                    it.setText(prefill)
                    if (wrapper.allowEmpty.not())
                        this@MIUIDialog.setActionButtonEnabled(WhichButton.POSITIVE, prefill?.isNotEmpty() == true || prefill?.isNotBlank() == true)
                }
                it.inputType = wrapper.inputType
                it.requestFocus()
                if (wrapper.multiLines)
                    it.inputType = it.inputType or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                it.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (!wrapper.waitForPositiveButton)
                            wrapper.callback?.invoke(s, this@MIUIDialog)
                        if (wrapper.allowEmpty.not())
                            this@MIUIDialog.setActionButtonEnabled(WhichButton.POSITIVE, s?.isNotEmpty() == true || s?.isNotBlank() == true)
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }

                })
            }
        }
    }

    private fun populatePositiveButton(view: View) {
        view.findViewById<Button>(R.id.miui_button_positive).let {
            it.gone()
            positiveWrapper?.let { wrapper ->
                it.visible()
                it.text = context.resolveText(res = wrapper.res, text = wrapper.text)
                it.setOnClickListener {
                    wrapper.click?.invoke(this)
                    if (inputWrapper?.waitForPositiveButton == true)
                        inputWrapper?.callback?.invoke(this.getInputField()?.text, this)
                    cancel()
                }
                view.findViewById<LinearLayout>(R.id.miui_action_panel).visible()
            }
        }
    }

    private fun populateNegativeButton(view: View) {
        view.findViewById<Button>(R.id.miui_button_negative).let {
            negativeWrapper?.let { wrapper ->
                it.visible()
                it.text = context.resolveText(res = wrapper.res, text = wrapper.text)
                it.setOnClickListener {
                    wrapper.click?.invoke(this)
                    cancel()
                }
                view.findViewById<LinearLayout>(R.id.miui_action_panel).visible()
            }
        }
    }
}

typealias DismissCallback = (MIUIDialog) -> Unit