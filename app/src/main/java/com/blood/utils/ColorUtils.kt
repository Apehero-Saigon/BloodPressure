package com.blood.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

class ColorUtils {
    companion object {
        fun Context.colorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)
    }
}