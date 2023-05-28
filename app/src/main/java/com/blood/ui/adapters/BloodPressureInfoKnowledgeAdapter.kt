package com.blood.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.blood.base.recyclerview.BaseRcvAdapter
import com.blood.base.recyclerview.BaseRecyclerViewListener
import com.blood.data.InfoKnowledge
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.ItemBloodPressureInfoKnowledgeBinding


class BloodPressureInfoKnowledgeAdapter(
    val list: List<InfoKnowledge>, val callback: BaseRecyclerViewListener<InfoKnowledge>? = null
) : BaseRcvAdapter<InfoKnowledge, ItemBloodPressureInfoKnowledgeBinding>(list, callback) {

    override fun createHolder(parent: ViewGroup) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_blood_pressure_info_knowledge,
            parent,
            false
        )
    )

    inner class ViewHolder(binding: ItemBloodPressureInfoKnowledgeBinding) :
        BaseViewHolder(binding) {

        override fun onBind(data: InfoKnowledge) {
            with(binding) {
                ivPhoto.setImageResource(data.photo)
                tvTitle.text = tvTitle.context.getText(data.title)
                itemView.clickWithDebounce {
                    callback?.onClick(data, absoluteAdapterPosition)
                }
            }
        }
    }
}