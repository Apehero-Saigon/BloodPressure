package com.blood.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import java.lang.Exception

object AppUtils {
    val Int.dp: Float get() = (this / Resources.getSystem().displayMetrics.density)

    val Int.px: Float get() = (this * Resources.getSystem().displayMetrics.density)
    fun dpToPx(dp: Int, context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics
    ).toInt()

    internal fun Context.getDrawableCompat(@DrawableRes drawable: Int) =
        ContextCompat.getDrawable(this, drawable)

    internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

    fun <T : Any> T?.isNull() = this == null

    fun <T : Any> T?.isNotNull() = this != null

    fun openWebsite(context: Context, url: String) {
        try {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            val intentChooser = Intent.createChooser(intent, "Select your browser to continue")
            context.startActivity(intentChooser)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}