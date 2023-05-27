package com.blood.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.blood.common.Constant
import com.blood.utils.LanguageUtils
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.ltl.apero.languageopen.Language

class LanguageAdapter(context: Context) : RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    private var isSelectedIndex: Int = 0

    private val languageListItems = LanguageUtils.languageListItems(context).apply {
        get(0).isChoose = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_language, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return languageListItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val language = languageListItems[position]

        holder.run {
            val typeface = ResourcesCompat.getFont(
                tvName.context,
                if (language.isChoose) R.font.montserrat_bold else R.font.montserrat_regular
            )
            tvName.typeface = typeface
            tvName.text = language.name

            ivCountry.setBackgroundResource(language.idIcon)

            ivCheckBox.isSelected = language.isChoose

            itemView.setOnClickListener {
                languageListItems[isSelectedIndex].isChoose = false
                languageListItems[holder.adapterPosition].isChoose = true
                notifyItemChanged(isSelectedIndex)
                notifyItemChanged(holder.adapterPosition)

                isSelectedIndex = holder.adapterPosition
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCountry: ImageView = view.findViewById(R.id.imgIconLanguage)
        val tvName: TextView = view.findViewById(R.id.txtNameLanguage)
        val ivCheckBox: ImageView = view.findViewById(R.id.imgChooseLanguage)
    }

    fun getSelectedLanguage(): Language? {
        return languageListItems.findLast { language -> language.isChoose }
    }

}