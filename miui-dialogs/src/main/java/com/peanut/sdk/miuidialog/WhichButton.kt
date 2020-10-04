package com.peanut.sdk.miuidialog;

enum class WhichButton(val index: Int) {
  POSITIVE(0),
  NEGATIVE(1);

  companion object {
    fun fromIndex(index: Int) = when (index) {
      0 -> POSITIVE
      1 -> NEGATIVE
      else -> throw IndexOutOfBoundsException("$index is not an action button index.")
    }
  }
}

