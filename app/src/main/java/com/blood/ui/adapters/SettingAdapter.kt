package com.blood.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blood.data.SettingMenu
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R

class SettingAdapter
constructor(val list: List<SettingMenu>, val callback: Callback? = null) :
    RecyclerView.Adapter<SettingAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val setting = list[position]

        holder.icon.setImageResource(setting.icon)
        holder.name.setText(setting.name)
        holder.layout.clickWithDebounce {
            callback?.onItemClick(setting)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val icon: ImageView = itemView.findViewById(R.id.elementIcon)
        val name: TextView = itemView.findViewById(R.id.elementName)
        val layout: LinearLayout = itemView.findViewById(R.id.elementLayout)
    }

    interface Callback {
        fun onItemClick(settingMenu: SettingMenu)
    }
}