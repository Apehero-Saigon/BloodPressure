package com.blood.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.blood.base.recyclerview.BaseRcvAdapter
import com.blood.base.recyclerview.BaseRecyclerViewListener
import com.blood.data.SettingMenu
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.ItemSettingBinding

class SettingAdapter(
    list: List<SettingMenu>, callback: BaseRecyclerViewListener<SettingMenu>? = null
) : BaseRcvAdapter<SettingMenu, ItemSettingBinding>(list, callback) {

    override fun createHolder(parent: ViewGroup) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_setting, parent, false
        )
    )

    inner class ViewHolder(binding: ItemSettingBinding) : BaseViewHolder(binding) {
        override fun onBind(data: SettingMenu) {
            with(binding) {
                ivIcon.setImageResource(data.icon)
                tvName.setText(data.name)
                itemView.clickWithDebounce {
                    listener?.onClick(data, absoluteAdapterPosition)
                }
            }
        }
    }
}