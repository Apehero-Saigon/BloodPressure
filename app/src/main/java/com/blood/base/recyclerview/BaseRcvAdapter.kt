package com.blood.base.recyclerview

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.blood.base.BaseData

abstract class BaseRcvAdapter<T : BaseData, B : ViewDataBinding>(
    var listItem: List<T>,
    var listener: BaseRecyclerViewListener<T>? = null
) : RecyclerView.Adapter<BaseRcvAdapter<T, B>.BaseViewHolder>() {

    abstract fun createHolder(parent: ViewGroup): BaseRcvAdapter<T, B>.BaseViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): BaseRcvAdapter<T, B>.BaseViewHolder {
        return createHolder(parent)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: BaseRcvAdapter<T, B>.BaseViewHolder, position: Int) {
        holder.onBind(listItem[holder.absoluteAdapterPosition])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(items: List<T>) {
        listItem = items
        notifyDataSetChanged()
    }

    open inner class BaseViewHolder(val binding: B) : RecyclerView.ViewHolder(binding.root) {

        open fun onBind(data: T) {

        }
    }
}