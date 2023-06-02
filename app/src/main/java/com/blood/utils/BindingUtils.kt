package com.blood.utils

import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blood.base.BaseData
import com.blood.base.recyclerview.BaseRcvAdapter
import com.blood.base.recyclerview.BaseRecyclerViewListener
import com.blood.common.Constant
import com.blood.data.BloodPressure
import com.blood.data.Language
import com.blood.data.Recommended
import com.blood.ui.adapters.BloodPressureAdapter
import com.blood.ui.adapters.LanguageAdapter
import com.blood.utils.AppUtils.isNotNull
import com.blood.utils.DateUtils.strDateTime
import com.blood.utils.customview.HeaderView
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shawnlin.numberpicker.NumberPicker
import java.lang.Exception
import java.util.Date

@BindingAdapter("bindDate")
fun TextView.bindTextTime(bindDate: Date) {
    if (bindDate.isNotNull()) {
        this.text = bindDate.strDateTime(Constant.FORMAT_DATETIME_VN)
    } else {
        this.text = ""
    }
}

@BindingAdapter("bindDateDDMMYYY")
fun TextView.bindDateDDMMYYY(date: Date) {
    if (date.isNotNull()) {
        this.text = date.strDateTime(Constant.FORMAT_DATE)
    } else {
        this.text = ""
    }
}

@BindingAdapter("bindDateHHMM")
fun TextView.bindDateHHMM(date: Date) {
    if (date.isNotNull()) {
        this.text = date.strDateTime(Constant.FORMAT_TIME)
    } else {
        this.text = ""
    }
}

@BindingAdapter("textInt")
fun TextView.textInt(int: Int?) {
    if (int.isNotNull()) {
        this.text = int.toString()
    } else {
        this.text = ""
    }
}

@BindingAdapter("bindNPValue")
fun NumberPicker.bindNPValue(value: Int?) {
    if (value.isNotNull()) {
        this.value = value!!
    }
}

@BindingAdapter("textNote")
fun TextView.textNote(note: String?) {
    text = context.getString(R.string.note_format, note)
}

@BindingAdapter("drawableRes")
fun setDrawableRes(imageView: ImageView, @DrawableRes drawableRes: Int) {
    imageView.setImageResource(drawableRes)
}

@BindingAdapter("textColorRes")
fun TextView.textColorRes(@ColorRes colorRes: Int?) {
    if (colorRes.isNotNull() && colorRes != 0) {
        setTextColor(ContextCompat.getColor(context, colorRes!!))
    }
}

@BindingAdapter("bindColorTint")
fun View.bindColorTint(@ColorRes colorRes: Int?) {
    if (colorRes.isNotNull() && colorRes != 0) {
        backgroundTintList = context.resources.getColorStateList(colorRes!!, null)
    }
}

@BindingAdapter("bindListBloodPressure", "listenerBlood")
fun RecyclerView.bindListBloodPressure(
    listItems: List<BloodPressure>, listener: BloodPressureAdapter.Callback
) {
    if (adapter == null) {
        adapter = BloodPressureAdapter(listItems, listener)
    } else {
        (adapter as? BaseRcvAdapter<BaseData, *>)?.updateData(listItems)
    }
}

@BindingAdapter("bindLanguages", "languageListener")
fun RecyclerView.bindLanguages(
    defaultSelected: String?, listener: BaseRecyclerViewListener<Language>? = null
) {
    val languages = LanguageUtils.languageListItems(this.context)
    if (!defaultSelected.isNullOrEmpty()) {
        for (language in languages) {
            if (defaultSelected.equals(language.code, true)) {
                language.isChoose = true
            }
        }
    }
    adapter = LanguageAdapter(languages, listener)
}

@BindingAdapter("visibleIfNotEmpty")
fun View.visibleIfNotEmpty(listItems: List<*>?) {
    this.visibility = if (listItems.isNullOrEmpty()) View.GONE else View.VISIBLE
}

@BindingAdapter("visibleIfEmpty")
fun View.visibleIfEmpty(listItems: List<*>?) {
    this.visibility = if (listItems.isNullOrEmpty()) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleIfNotZero")
fun View.visibleIfNotZero(int: Int?) {
    this.visibility = if (int != null && int > 0) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleIfZero")
fun View.visibleIfZero(int: Int?) {
    this.visibility = if (int == null || int <= 0) View.VISIBLE else View.GONE
}

@BindingAdapter("headerListener")
fun HeaderView.headerListener(listener: HeaderView.Listener?) {
    this.listener = listener
}

@BindingAdapter("bindRecommendation", "languageCode", requireAll = true)
fun WebView.bindRecommendation(name: String, languageCode: String) {
    try {
        val jsonFileString = AssetUtils.getJsonDataFromAsset(
            this.context, "languages/recommends_${languageCode}.json"
        )

        val listPersonType = object : TypeToken<List<Recommended>>() {}.type
        val listRecommended: List<Recommended> = Gson().fromJson(jsonFileString, listPersonType)
        val recommended = listRecommended.findLast { it -> it.name.equals(name, true) }
        this.loadData(
            recommended?.content ?: listRecommended[0].content, "text/html", "UTF-8"
        )
    } catch (_: Exception) {

    }
}