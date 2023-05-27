package com.blood.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blood.data.InfoKnowledge
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R

class BloodPressureInfoKnowledgeAdapter(
    var list: List<InfoKnowledge>, val callback: Callback? = null
) : RecyclerView.Adapter<BloodPressureInfoKnowledgeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_blood_pressure_info_knowledge, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val infoKnowledge = list[position]

        holder.photo.setImageResource(infoKnowledge.photo)
        holder.title.text = holder.itemView.context.getText(infoKnowledge.title)
        holder.itemView.clickWithDebounce {
            callback?.onInfoKnowLedgeClicked(infoKnowledge)
        }
    }

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val photo: ImageView = itemView.findViewById(R.id.ivPhoto)
        val title: TextView = itemView.findViewById(R.id.tvTitle)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDate(list: ArrayList<InfoKnowledge>) {
        this.list = list
        notifyDataSetChanged()
    }

    interface Callback {
        fun onInfoKnowLedgeClicked(infoKnowledge: InfoKnowledge)
    }
}