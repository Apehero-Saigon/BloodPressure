package com.blood.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.blood.base.recyclerview.BaseRcvAdapter
import com.blood.base.recyclerview.BaseRecyclerViewListener
import com.blood.data.LimitValue
import com.blood.utils.ColorUtils.Companion.colorCompat
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.ItemLimitValueBinding

class LimitValueAdapter(callback: BaseRecyclerViewListener<LimitValue>? = null) :
    BaseRcvAdapter<LimitValue, ItemLimitValueBinding>(mutableListOf(), callback) {

    override fun createHolder(parent: ViewGroup) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_limit_value, parent, false
        )
    )

    inner class ViewHolder(binding: ItemLimitValueBinding) : BaseViewHolder(binding) {

        override fun onBind(data: LimitValue) {
            with(binding) {
                val context = binding.root.context

                tvName.setText(data.name)

                tvSys.text = data.sys!!.getTextMaxMin()
                tvSys.background.setTint(context.colorCompat(data.sys!!.color))

                tvDia.text = data.dia!!.getTextMaxMin()
                tvDia.background.setTint(context.colorCompat(data.dia!!.color))

                tvOpera.setText(data.opera)
            }
        }
    }
}