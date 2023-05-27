package com.blood.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter

object BindingUtils {
    @BindingAdapter("drawableRes")
    fun setDrawableRes(imageView: ImageView, @DrawableRes drawableRes: Int) {
        imageView.setImageResource(drawableRes)
    }
}