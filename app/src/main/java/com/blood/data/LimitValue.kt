package com.blood.data

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.blood.base.BaseData
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R


data class LimitValue(
    var sys: Limit, var dia: Limit, @StringRes var name: Int, @StringRes var opera: Int
) : BaseData {
    companion object {
        fun getList2017ACCAHA(): List<LimitValue> {
            val list = mutableListOf<LimitValue>()
            list.add(
                LimitValue(
                    Limit(90, 0, R.color.color_5C4493),
                    Limit(60, 0, R.color.color_5C4493),
                    R.string.low,
                    R.string.and
                )
            )
            list.add(
                LimitValue(
                    Limit(0, 120, R.color.color_47D598),
                    Limit(0, 80, R.color.color_47D598),
                    R.string.normal,
                    R.string.and
                )
            )
            list.add(
                LimitValue(
                    Limit(120, 129, R.color.color_FFC543),
                    Limit(0, 80, R.color.color_FFC543),
                    R.string.elevated,
                    R.string.and
                )
            )
            list.add(
                LimitValue(
                    Limit(130, 139, R.color.color_FF7A00),
                    Limit(80, 89, R.color.color_FF7A00),
                    R.string.hypertension_stage_1,
                    R.string.and
                )
            )
            list.add(
                LimitValue(
                    Limit(140, 179, R.color.color_FF2C2C),
                    Limit(90, 119, R.color.color_FF2C2C),
                    R.string.hypertension_stage_2,
                    R.string.and
                )
            )
            list.add(
                LimitValue(
                    Limit(0, 180, R.color.color_E20000),
                    Limit(0, 120, R.color.color_E20000),
                    R.string.hypertension_stage_3,
                    R.string.and_or
                )
            )
            return list
        }

        fun getList2018ESCESH(): List<LimitValue> {
            val list = mutableListOf<LimitValue>()
            list.add(
                LimitValue(
                    Limit(90, 0, R.color.color_5C4493),
                    Limit(60, 0, R.color.color_5C4493),
                    R.string.low,
                    R.string.and
                )
            )
            list.add(
                LimitValue(
                    Limit(0, 120, R.color.color_AECA53),
                    Limit(0, 80, R.color.color_AECA53),
                    R.string.optimal,
                    R.string.and
                )
            )
            list.add(
                LimitValue(
                    Limit(120, 129, R.color.color_47D598),
                    Limit(80, 84, R.color.color_47D598),
                    R.string.normal,
                    R.string.and
                )
            )
            list.add(
                LimitValue(
                    Limit(130, 139, R.color.color_47D598),
                    Limit(85, 89, R.color.color_47D598),
                    R.string.high_normal,
                    R.string.and
                )
            )
            list.add(
                LimitValue(
                    Limit(120, 129, R.color.color_FFC543),
                    Limit(0, 80, R.color.color_FFC543),
                    R.string.elevated,
                    R.string.and
                )
            )
            list.add(
                LimitValue(
                    Limit(140, 159, R.color.color_FF7A00),
                    Limit(90, 99, R.color.color_FF7A00),
                    R.string.hypertension_stage_1,
                    R.string.and
                )
            )
            list.add(
                LimitValue(
                    Limit(160, 179, R.color.color_FF2C2C),
                    Limit(100, 109, R.color.color_FF2C2C),
                    R.string.hypertension_stage_2,
                    R.string.and
                )
            )
            list.add(
                LimitValue(
                    Limit(0, 180, R.color.color_E20000),
                    Limit(0, 120, R.color.color_E20000),
                    R.string.hypertension_stage_3,
                    R.string.and_or
                )
            )
            return list
        }
    }
}

data class Limit(var max: Int = 0, var min: Int = 0, @ColorRes var color: Int) {
    fun getTextMaxMin(): String {
        return if (max > 0 && min > 0) {
            "$min - $max"
        } else if (max > 0) {
            "<$max"
        } else if (min > 0) {
            ">$min"
        } else {
            ""
        }
    }
}