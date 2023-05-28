package com.blood.base.recyclerview

import com.blood.base.BaseData

interface BaseRecyclerViewListener<T : BaseData> {
    fun onClick(data: T, position: Int) {

    }
}