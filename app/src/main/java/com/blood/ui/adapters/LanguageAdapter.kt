package com.blood.ui.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.blood.base.recyclerview.BaseRcvAdapter
import com.blood.base.recyclerview.BaseRecyclerViewListener
import com.blood.data.Language
import com.blood.utils.ColorUtils.Companion.colorCompat
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.ItemLanguageBinding

class LanguageAdapter(
    list: List<Language>, callback: BaseRecyclerViewListener<Language>? = null
) : BaseRcvAdapter<Language, ItemLanguageBinding>(list, callback) {

    private var isSelectedIndex: Int = 0
    override fun createHolder(parent: ViewGroup) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_language, parent, false
        )
    )

    inner class ViewHolder(binding: ItemLanguageBinding) : BaseViewHolder(binding) {

        override fun onBind(data: Language) {
            with(binding) {
                val context = rootView.context
                val typeface = ResourcesCompat.getFont(
                    context,
                    if (data.isChoose) R.font.montserrat_bold else R.font.montserrat_regular
                )
                tvName.typeface = typeface
                tvName.text = data.name

                ivCountry.setBackgroundResource(data.idIcon)
                if (data.isChoose) {
                    tvName.setTextColor(context.colorCompat(R.color.white))
                    rootView.background =
                        AppCompatResources.getDrawable(context, R.drawable.bg_fe7489_radius_50)
                } else {
                    tvName.setTextColor(context.colorCompat(R.color.black))
                    rootView.background =
                        AppCompatResources.getDrawable(context, R.drawable.bg_f8f8f8_radius_50)
                }
                itemView.setOnClickListener {
                    listItem[isSelectedIndex].isChoose = false
                    listItem[absoluteAdapterPosition].isChoose = true
                    notifyItemChanged(isSelectedIndex)
                    notifyItemChanged(absoluteAdapterPosition)

                    isSelectedIndex = absoluteAdapterPosition
                }
            }
        }
    }

    fun getSelectedLanguage(): Language? {
        return listItem.findLast { language -> language.isChoose }
    }
}