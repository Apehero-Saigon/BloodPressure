package com.blood.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter

object BindingUtils {
    @BindingAdapter("textLiveText")
    fun setText(textView: TextView, text: String) {
        textView.text = text
    }
}