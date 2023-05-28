package com.blood.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.blood.base.recyclerview.BaseRcvAdapter
import com.blood.base.recyclerview.BaseRecyclerViewListener
import com.blood.common.Constant
import com.blood.data.BloodPressure
import com.blood.utils.DateUtils.strDateTime
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.ItemBloodPressureBinding

class BloodPressureAdapter(val list: List<BloodPressure>, val callback: Callback? = null) :
    BaseRcvAdapter<BloodPressure, ItemBloodPressureBinding>(list, callback) {

    override fun createHolder(parent: ViewGroup) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_blood_pressure, parent, false
        )
    )

    inner class ViewHolder(binding: ItemBloodPressureBinding) : BaseViewHolder(binding) {

        override fun onBind(bloodPressure: BloodPressure) {
            with(binding) {
                tvSystoleAndDiastole.text = bloodPressure.getSystoleAndDiastole()
                tvPulse.text = bloodPressure.pulse.toString()
                tvConclusion.setText(bloodPressure.getStatus())
                tvConclusion.setTextColor(
                    ContextCompat.getColor(
                        tvPulse.context, bloodPressure.getStatusColor()
                    )
                )
                tvCreatedAt.text = tvPulse.context.getString(
                    R.string.measured_at_format,
                    bloodPressure.createAt.strDateTime(Constant.FORMAT_DATETIME_ITEM)
                )
                itemView.clickWithDebounce {
                    callback?.onClick(bloodPressure, absoluteAdapterPosition)
                }
            }
        }
    }

    interface Callback : BaseRecyclerViewListener<BloodPressure>
}