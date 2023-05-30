package com.blood.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blood.base.recyclerview.BaseRcvAdapter
import com.blood.common.Constant
import com.blood.data.BloodPressure
import com.blood.ui.adapters.BloodPressureAdapter
import com.blood.utils.AppUtils.isNotNull
import com.blood.utils.DateUtils.strDateTime
import com.blood.utils.customview.HeaderView
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.shawnlin.numberpicker.NumberPicker
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
        @BindingAdapter("bindDateDDMMYYY")
        fun TextView.bindDateDDMMYYY(date: Date) {
            if (date.isNotNull()) {
                this.text = date.strDateTime(Constant.FORMAT_DATE)
            } else {
                this.text = ""
            }
        }

        @JvmStatic
        @BindingAdapter("bindDateHHMM")
        fun TextView.bindDateHHMM(date: Date) {
            if (date.isNotNull()) {
                this.text = date.strDateTime(Constant.FORMAT_TIME)
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
        @BindingAdapter("bindNPValue")
        fun NumberPicker.bindNPValue(value: Int?) {
            if (value.isNotNull()) {
                this.value = value!!
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
        fun RecyclerView.bindListBloodPressure(
            listItems: List<BloodPressure>, listener: BloodPressureAdapter.Callback
        ) {
            if (adapter == null) {
                adapter = BloodPressureAdapter(listItems, listener)
            } else {
                (adapter as? BaseRcvAdapter<BloodPressure, *>)?.updateData(listItems)
            }
        }

        @JvmStatic
        @BindingAdapter("visibleIfNotEmpty")
        fun View.visibleIfNotEmpty(listItems: List<*>?) {
            this.visibility = if (listItems.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        @JvmStatic
        @BindingAdapter("visibleIfEmpty")
        fun View.visibleIfEmpty(listItems: List<*>?) {
            this.visibility = if (listItems.isNullOrEmpty()) View.VISIBLE else View.GONE
        }

        @JvmStatic
        @BindingAdapter("visibleIfNotZero")
        fun View.visibleIfNotZero(int: Int?) {
            this.visibility = if (int != null && int > 0) View.VISIBLE else View.GONE
        }

        @JvmStatic
        @BindingAdapter("visibleIfZero")
        fun View.visibleIfZero(int: Int?) {
            this.visibility = if (int == null || int <= 0) View.VISIBLE else View.GONE
        }

        @JvmStatic
        @BindingAdapter("headerListener")
        fun HeaderView.headerListener(listener: HeaderView.Listener?) {
            this.listener = listener
        }

    }
}