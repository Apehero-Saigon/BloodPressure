package com.blood.utils

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blood.base.BaseData
import com.blood.base.recyclerview.BaseRcvAdapter
import com.blood.base.recyclerview.BaseRecyclerViewListener
import com.blood.common.Constant
import com.blood.data.BloodPressure
import com.blood.ui.adapters.BloodPressureAdapter
import com.blood.utils.AppUtils.isNotNull
import com.blood.utils.DateUtils.strDateTime
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import java.util.Date

class BindingUtils {
    companion object {

        @JvmStatic
        @BindingAdapter("bindDate")
        fun TextView.bindTextTime(bindDate: Date) {
            if (bindDate.isNotNull()) {
                this.text = bindDate.strDateTime(Constant.FORMAT_DATETIME_VN)
            } else {
                this.text = ""
            }
        }

        @JvmStatic
        @BindingAdapter("textInt")
        fun TextView.textInt(int: Int?) {
            if (int.isNotNull()) {
                this.text = int.toString()
            } else {
                this.text = ""
            }
        }

        @JvmStatic
        @BindingAdapter("textNote")
        fun TextView.textNote(note: String?) {
            text = context.getString(R.string.note_format, note)
        }

        @JvmStatic
        @BindingAdapter("drawableRes")
        fun setDrawableRes(imageView: ImageView, @DrawableRes drawableRes: Int) {
            imageView.setImageResource(drawableRes)
        }

        @JvmStatic
        @BindingAdapter("textColorRes")
        fun TextView.textColorRes(@ColorRes colorRes: Int?) {
            if (colorRes.isNotNull() && colorRes != 0) {
                setTextColor(ContextCompat.getColor(context, colorRes!!))
            }
        }

        @JvmStatic
        @BindingAdapter("bindColorTint")
        fun View.bindColorTint(@ColorRes colorRes: Int?) {
            if (colorRes.isNotNull() && colorRes != 0) {
                backgroundTintList = context.resources.getColorStateList(colorRes!!, null)
            }
        }

        @JvmStatic
        @BindingAdapter("bindListBloodPressure", "listenerBlood")
        fun RecyclerView.updateDataBloodPressure(
            listItems: List<BloodPressure>, listener: BloodPressureAdapter.Callback
        ) {
            if (adapter == null) {
                adapter = BloodPressureAdapter(listItems, listener)
            } else {
                (adapter as? BaseRcvAdapter<BloodPressure, *>)?.updateData(listItems)
            }
        }
    }
}