package com.peanut.sdk.miuidialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.callbacks.onPreShow
import com.afollestad.materialdialogs.callbacks.onShow
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.utils.MDUtil
import com.afollestad.materialdialogs.utils.MDUtil.resolveColor
import com.peanut.sdk.miuidialog.AddInFunction.createDrawable
import com.peanut.sdk.miuidialog.AddInFunction.createView
import com.peanut.sdk.miuidialog.AddInFunction.gone
import com.peanut.sdk.miuidialog.AddInFunction.resolveLayout
import com.peanut.sdk.miuidialog.AddInFunction.resolveText
import com.peanut.sdk.miuidialog.AddInFunction.visible
import com.peanut.sdk.miuidialog.MIUIVersion.MIUI11
import com.peanut.sdk.miuidialog.content_wrapper.*


typealias MIUICallback = (MIUIDialog) -> Unit

/**
 * 创建一个MIUIDialog
 *
 * @param miuiVersion miui版本号, 不同的版本号的UI不一样, 代码都是一样的
 */
class MIUIDialog(private val context: Context, private val miuiVersion: Int = MIUI11) {
    private var dialog: MaterialDialog? = null

    private var iconWrapper: IconWrapper? = null
    private var titleWrapper: TitleWrapper? = null
    private var inputWrapper: InputWrapper? = null
    private var messageWrapper: MessageWrapper? = null
    private var messageIconWrapper: MessageIconWrapper? = null
    private var positiveWrapper: PositiveWrapper? = null
    private var negativeWrapper: NegativeWrapper? = null
    private var progressWrapper: ProgressWrapper? = null
    private var customViewWrapper: CustomViewWrapper? = null

    var cancelOnTouchOutside: Boolean = true
    var cancelable: Boolean = true

    private var miuiView: View? = null
    private var miuiLight: Boolean = true

    /**
     * 首先初始化view
     */
    private fun initialize(){
        calculateVisionLight()
        //处理不同的MIUI版本
        miuiView = when (miuiVersion) {
            MIUI11 -> context.resolveLayout(miuiLight, dayLayoutRes = R.layout.miui11layout, nightLayoutRes = R.layout.miui11layout_night)
            else -> throw IllegalStateException("Only MIUI-11 supported yet!")
        }
        dialog = MaterialDialog(context, BottomSheet(layoutMode = LayoutMode.WRAP_CONTENT))
    }

    /**
     * Shows an drawable to the top of the dialog title.
     *
     * @param res The drawable resource to display as the drawable.
     * @param drawable The drawable to display as the drawable.
     */
    fun icon(@DrawableRes res: Int? = null, drawable: Drawable? = null): MIUIDialog = apply {
        MDUtil.assertOneSet("icon", drawable, res)
        this.iconWrapper = IconWrapper(res, drawable)
    }

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
        MDUtil.assertOneSet("message", text, res)
        messageWrapper = MessageWrapper(res, text, messageSetting).apply {
            messageSetting?.invoke(this.MessageSettings())
        }
    }

    /**
     * Shows an drawable to the bottom of the dialog message.
     *
     * @param res The drawable resource to display as the drawable.
     * @param drawable The drawable to display as the drawable.
     */
    fun messageImg(@DrawableRes res: Int? = null, drawable: Drawable? = null): MIUIDialog = apply {
        MDUtil.assertOneSet("messageIcon", drawable, res)
        this.messageIconWrapper = MessageIconWrapper(res, drawable)
    }

    /**
     * Sets a custom view to display in the dialog, below the title and above the action buttons
     * (and checkbox prompt).
     *
     * @param viewRes The layout resource to inflate as the custom view.
     * @param view The view to insert as the custom view.
//     * @param scrollable Whether or not the custom view is automatically wrapped in a ScrollView.
//     * @param noVerticalPadding When set to true, vertical padding is not added around your content.
//     * @param horizontalPadding When true, 24dp horizontal padding is applied to your custom view.
//     * @param dialogWrapContent When true, the dialog will wrap the content width.
     */
    fun customView(
            @LayoutRes viewRes: Int? = null,
            view: View? = null,
            func: viewHandle? = null
    ): MIUIDialog = apply {
        MDUtil.assertOneSet("customView", view, viewRes)
        this.customViewWrapper = CustomViewWrapper(viewRes, view,func = func)
    }

    /**
     * Shows a positive action button, in the far right at the bottom of the dialog.
     *
     * @param res The string resource to display on the title.
     * @param text The literal string to display on the button.
     * @param click A listener to invoke when the button is pressed.
     */
    fun positiveButton(@StringRes res: Int? = null, text: CharSequence? = null, countdown: Int? = null, click: PositiveCallback? = null): MIUIDialog = apply {
        this.positiveWrapper = PositiveWrapper(res, text, countdown, click)
    }

    /**
     * Shows a negative action button, to the left of the positive action button (or at the far
     * right if there is no positive action button).
     *
     * @param res The string resource to display on the title.
     * @param text The literal string to display on the button.
     * @param click A listener to invoke when the button is pressed.
     */
    fun negativeButton(@StringRes res: Int? = null, text: CharSequence? = null, countdown: Int? = null, click: NegativeCallback? = null): MIUIDialog = apply {
        this.negativeWrapper = NegativeWrapper(res, text, countdown, click)
    }

    /** Applies multiple properties to the dialog and opens it. */
    fun show(func: MIUIDialog.() -> Unit): MIUIDialog = apply {
        initialize()
        func()
        this.show()
    }

    /**
     * Adds a listener that's invoked when the dialog is [MaterialDialog.dismiss]'d. If this is called
     * multiple times, it appends additional callbacks, rather than overwriting.
     */
    fun MIUIDialog.onDismiss(MIUICallback: MIUICallback): MIUIDialog = apply {
        dialog?.onDismiss {
            MIUICallback.invoke(this)
        }
    }

    /**
     * Adds a listener that's invoked when the dialog is [MaterialDialog.show]'n. If this is called
     * multiple times, it appends additional callbacks, rather than overwriting.
     *
     * If the dialog is already showing, the callback be will be invoked immediately.
     */
    fun MIUIDialog.onShow(MIUICallback: MIUICallback): MIUIDialog = apply {
        dialog?.onShow {
            MIUICallback.invoke(this)
        }
    }

    /**
     * Adds a listener that's invoked right before the dialog is [MaterialDialog.show]'n. If this is called
     * multiple times, it appends additional callbacks, rather than overwriting.
     */
    fun MIUIDialog.onPreShow(MIUICallback: MIUICallback): MIUIDialog = apply {
        dialog?.onPreShow {
            MIUICallback.invoke(this)
        }
    }

    /**
     * Adds a listener that's invoked when the dialog is canceled. If this is called
     * multiple times, it overwriting.
     */
    fun MIUIDialog.onCancel(MIUICallback: MIUICallback): MIUIDialog = apply {
        dialog?.onCancel {
            MIUICallback.invoke(this)
        }
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
    fun MIUIDialog.input(
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

    /** Enables or disables an action button. */
    fun setActionButtonEnabled(
            which: WhichButton,
            enabled: Boolean
    ) {
        when(which){
            WhichButton.POSITIVE -> {
                miuiView?.findViewById<Button>(R.id.miui_button_positive)?.let {
                    it.isEnabled = enabled
                    it.setTextColor(if (enabled) Color.parseColor("#0b94f2") else Color.GRAY)
                }
            }
            WhichButton.NEGATIVE -> {
                miuiView?.findViewById<Button>(R.id.miui_button_negative)?.let {
                    it.isEnabled = enabled
                    it.setTextColor(if (enabled) ThemedColor.mainColor(miuiLight) else Color.GRAY)
                }
            }
        }
    }

    /**
     * show a progress
     */
    fun MIUIDialog.progress(@StringRes res: Int? = null, text: CharSequence? = null): MIUIDialog = apply {
        this.progressWrapper = ProgressWrapper(res, text)
        cancelOnTouchOutside = false
        cancelable = false
    }

    /**
     * re-set progress text
     */
    fun setProgressText(text: String){
        miuiView?.findViewById<TextView>(R.id.progress_text)?.text = text
    }

    /**
     * Cancel the dialog.
     */
    fun cancel() = dialog?.cancel()

    /**
     * dismiss the dialog.
     */
    fun dismiss() = dialog?.dismiss()

    /**
     * Gets the input EditText for the dialog.
     */
    private val inputField:EditText?
        get() = miuiView?.findViewById(R.id.miui_input)

    /**
     * Gets the input EditText for the dialog.
     */
    fun MIUIDialog.getInputField():EditText? = miuiView?.findViewById(R.id.miui_input)

    /**
     * Gets the message TextView for the dialog.
     */
    fun MIUIDialog.getMessageTextView():TextView? = miuiView?.findViewById(R.id.miui_message)

    /**
     * 设置输入框的错误状态下的提示
     * 还需要改变输入框的背景
     */
    fun setInputError(text: String?) {
        miuiView?.let {
            val a = it.findViewById<TextView>(R.id.miui_input_error_msg)
            a.gone()
            if (text.isNullOrEmpty())
                inputField?.background =
                        if (miuiLight) R.drawable.miui_input_bg.createDrawable(context)
                        else R.drawable.miui_input_bg_dark.createDrawable(context)
            else {
                inputField?.background =
                        if (miuiLight) R.drawable.miui_input_bg_err.createDrawable(context)
                        else R.drawable.miui_input_bg_err_dark.createDrawable(context)
                a.visible()
                a.text = text
            }
        }
    }

    private fun calculateVisionLight() {
        //处理主题色
        miuiLight = resolveColor(attr = R.attr.md_background_color, context = context) {
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

    private fun show() {
        miuiView?.let {
            populateIcon(it)
            populateTitle(it)
            populateMessage(it)
            populateMessageIcon(it)
            populateInput(it)
            populatePositiveButton(it)
            populateNegativeButton(it)
            populateActionButton(it)
            populateProgress(it)
            populateCustomView(it)
        }
        dialog?.show {
            customView(view = miuiView, noVerticalPadding = true, scrollable = true)
            cancelable(this@MIUIDialog.cancelable)
            cancelOnTouchOutside(this@MIUIDialog.cancelOnTouchOutside)
            miuiView?.post {
                setPeekHeight(miuiView?.height)
            }
        }
    }

    private fun populateCustomView(view: View) {
        view.findViewById<LinearLayout>(R.id.miui_custom_view).let {
            it.gone()
            customViewWrapper?.let { wrapper ->
                val v = wrapper.view ?: wrapper.viewRes!!.createView(context)
                it.addView(v,0)
                it.visible()
                wrapper.func?.invoke(v)
            }
        }
    }

    private fun populateActionButton(it: View) {
        if (positiveWrapper == null && negativeWrapper == null)
            it.findViewById<LinearLayout>(R.id.miui_action_panel).gone()
        else it.findViewById<LinearLayout>(R.id.miui_action_panel).visible()
    }

    private fun populateIcon(view: View) {
        view.findViewById<ImageView>(R.id.miui_icon).let {
            it.gone()
            iconWrapper?.let { wrapper ->
                wrapper.res?.let { res->
                    it.setImageResource(res)
                }
                wrapper.drawable?.let { drawable->
                    it.setImageDrawable(drawable)
                }
                it.visible()
            }
        }
    }

    private fun populateTitle(view: View) {
        view.findViewById<TextView>(R.id.miui_title).let {
            it.gone()
            titleWrapper?.let { wrapper ->
                it.text = context.resolveText(res = wrapper.res, text = wrapper.text)
                it.visible()
            }
        }
    }

    private fun populateMessage(view: View) {
        view.findViewById<TextView>(R.id.miui_message).let {
            it.gone()
            messageWrapper?.populate(it, context)
        }
    }

    private fun populateMessageIcon(view: View) {
        view.findViewById<ImageView>(R.id.miui_message_img).let {
            it.gone()
            messageIconWrapper?.let { wrapper ->
                wrapper.res?.let { res->
                    it.setImageResource(res)
                }
                wrapper.drawable?.let { drawable->
                    it.setImageDrawable(drawable)
                }
                it.visible()
            }
        }
    }

    private fun populateInput(view: View) {
        view.findViewById<EditText>(R.id.miui_input).let {
            it.gone()
            inputWrapper?.populate(it, context, this)
        }
    }

    private fun populatePositiveButton(view: View) {
        view.findViewById<Button>(R.id.miui_button_positive).let {
            it.gone()
            positiveWrapper?.let { wrapper ->
                it.visible()
                val userText = context.resolveText(res = wrapper.res, text = wrapper.text)
                it.text = userText
                wrapper.countdown?.let { second->
                    object :Thread(){
                        override fun run() {
                            var i = -1
                            while (second-++i>0){
                                Handler(context.mainLooper).post {
                                    this@MIUIDialog.setActionButtonEnabled(WhichButton.POSITIVE, false)
                                    it.text = String.format("%s(%d)", userText, second - i)
                                }
                                sleep(1000)
                            }
                            Handler(context.mainLooper).post {
                                it.text = userText
                                this@MIUIDialog.setActionButtonEnabled(WhichButton.POSITIVE, true)
                            }
                        }
                    }.start()
                }
                it.setOnClickListener {
                    wrapper.click?.invoke(this)
                    if (inputWrapper?.waitForPositiveButton == true){
                        inputWrapper?.callback?.invoke(this.inputField?.text, this)
                    }
                    cancel()
                }
            }
        }
    }

    private fun populateNegativeButton(view: View) {
        view.findViewById<Button>(R.id.miui_button_negative).let {
            it.gone()
            negativeWrapper?.let { wrapper ->
                it.visible()
                val userText = context.resolveText(res = wrapper.res, text = wrapper.text)
                it.text = userText
                wrapper.countdown?.let { second->
                    object :Thread(){
                        override fun run() {
                            var i = -1
                            while (second-++i>0){
                                Handler(context.mainLooper).post {
                                    it.text = String.format("%s (%d)", userText, second - i)
                                }
                                sleep(1000)
                            }
                            Handler(context.mainLooper).post {
                                it.text = userText
                                if(dialog?.isShowing == true)
                                    it.performClick()
                            }
                        }
                    }.start()
                }
                it.setOnClickListener {
                    wrapper.click?.invoke(this)
                    cancel()
                }
            }
        }
    }

    private fun populateProgress(view: View) {
        view.findViewById<ConstraintLayout>(R.id.miui_progress).let {
            it.gone()
            progressWrapper?.let { wrapper ->
                it.findViewById<TextView>(R.id.progress_text).text = context.resolveText(res = wrapper.res, text = wrapper.text)
                //rotate icon
                val icon = it.findViewById<ImageView>(R.id.progress_icon)
                icon.post {
                    Log.v("weight",(icon.width/2).toString())
                    Log.v("weight1",(icon.height/2).toString())
                    val rotateAnimation: Animation = RotateAnimation(0f, 360f, (icon.width/2).toFloat(), (icon.height/2).toFloat())
                    rotateAnimation.duration = 600
                    rotateAnimation.repeatCount = -1
                    rotateAnimation.interpolator = LinearInterpolator()
                    icon.startAnimation(rotateAnimation)
                }
                it.visible()
            }
        }
    }
}