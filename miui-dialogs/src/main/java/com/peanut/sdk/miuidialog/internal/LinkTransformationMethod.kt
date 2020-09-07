package com.peanut.sdk.miuidialog.internal

import android.graphics.Rect
import android.text.Spannable
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.method.TransformationMethod
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.internal.message.CustomUrlSpan

/** https://medium.com/@nullthemall/make-textview-open-links-in-customtabs-12fdcf4bb684 */
internal class LinkTransformationMethod(
  private val onLinkClick: (link: String) -> Unit
) : TransformationMethod {
  override fun getTransformation(
    source: CharSequence,
    view: View
  ): CharSequence {
    if (view !is TextView) {
      return source
    } else if (view.text == null || view.text !is Spannable) {
      return source
    }
    val text = view.text as Spannable
    val spans = text.getSpans(0, view.length(), URLSpan::class.java)
    for (span in spans) {
      val start = text.getSpanStart(span)
      val end = text.getSpanEnd(span)
      val url = span.url

      text.removeSpan(span)
      text.setSpan(CustomUrlSpan(url, onLinkClick), start, end, SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return text
  }

  override fun onFocusChanged(
    view: View,
    sourceText: CharSequence,
    focused: Boolean,
    direction: Int,
    previouslyFocusedRect: Rect
  ) = Unit
}