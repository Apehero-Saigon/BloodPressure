package com.blood.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.blood.base.recyclerview.BaseRcvAdapter
import com.blood.base.recyclerview.BaseRecyclerViewListener
import com.blood.data.Language
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.ItemLanguageSettingBinding

class LanguageSettingAdapter(
    list: List<Language>, callback: BaseRecyclerViewListener<Language>? = null
) : BaseRcvAdapter<Language, ItemLanguageSettingBinding>(list, callback) {

    override fun createHolder(parent: ViewGroup) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_language_setting, parent, false
        )
    )

    inner class ViewHolder(binding: ItemLanguageSettingBinding) : BaseViewHolder(binding) {

        override fun onBind(data: Language) {
            with(binding) {
                cbLanguage.visibility = if (data.isChoose) View.VISIBLE else View.INVISIBLE
                tvName.text = data.name
                itemView.clickWithDebounce {
                    if (!data.isChoose) {
                        listener?.onClick(data, absoluteAdapterPosition)
                    }
                }
            }
        }
    }
}