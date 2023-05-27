package com.blood.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.ltl.apero.languageopen.Language

class LanguageSettingAdapter(val list: List<Language>, val callback: Callback? = null) :
    RecyclerView.Adapter<LanguageSettingAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_language_setting, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val language = list[position]

        holder.cbLanguage.visibility = if (language.isChoose) View.VISIBLE else View.INVISIBLE
        holder.name.text = language.name
        holder.itemView.clickWithDebounce {
            for (element in list.indices) {
                list[element].isChoose = element == position
            }
            notifyDataSetChanged()
            callback?.onLanguageClicked(language.code)
        }
    }

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val cbLanguage: ImageView = itemView.findViewById(R.id.cbLanguage)
    }

    interface Callback {

        fun onLanguageClicked(languageCode: String)
    }
}