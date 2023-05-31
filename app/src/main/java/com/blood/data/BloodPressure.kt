package com.blood.data

import com.blood.base.BaseData
import com.blood.common.Constant
import com.blood.utils.PrefUtils
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import java.io.Serializable
import java.util.Date

data class BloodPressure(
    var profileId: Long = 0,
    var systole: Int = 0,
    var diastole: Int = 0,
    var pulse: Int = 0,
    var createAt: Date = Date(),
    var note: String = "",
    var id: Long = 0
) : Serializable, BaseData {

    companion object {
        @JvmStatic
        fun getColorSystolic(systole: Int): Int {
            return if (PrefUtils.instant.typeLimitValue == Constant.ACC_AHA_2017) {
                LimitValue.getList2017ACCAHA().map { it.sys }.toMutableList()
                    .findLast { sys -> sys.contains(systole) }!!.color
            } else {
                LimitValue.getList2018ESCESH().map { it.sys }.toMutableList()
                    .findLast { sys -> sys.contains(systole) }!!.color
            }
        }

        @JvmStatic
        fun getColorDiastolic(diastole: Int): Int {
            return if (PrefUtils.instant.typeLimitValue == Constant.ACC_AHA_2017) {
                LimitValue.getList2017ACCAHA().map { it.dia }.toMutableList()
                    .findLast { sys -> sys.contains(diastole) }!!.color
            } else {
                LimitValue.getList2018ESCESH().map { it.dia }.toMutableList()
                    .findLast { sys -> sys.contains(diastole) }!!.color
            }
        }
    }

    fun getTextDiastoleMaxMin(): String {
        return if (PrefUtils.instant.typeLimitValue == Constant.ACC_AHA_2017) {
            LimitValue.getList2017ACCAHA().map { it.dia }.toMutableList()
                .findLast { sys -> sys.contains(diastole) }!!.getTextMaxMin()
        } else {
            LimitValue.getList2018ESCESH().map { it.dia }.toMutableList()
                .findLast { sys -> sys.contains(diastole) }!!.getTextMaxMin()
        }
    }

    fun getTextSystoleMaxMin(): String {
        return if (PrefUtils.instant.typeLimitValue == Constant.ACC_AHA_2017) {
            LimitValue.getList2017ACCAHA().map { it.sys }.toMutableList()
                .findLast { sys -> sys.contains(systole) }?.getTextMaxMin() ?: ""
        } else {
            LimitValue.getList2018ESCESH().map { it.sys }.toMutableList()
                .findLast { sys -> sys.contains(systole) }?.getTextMaxMin() ?: ""
        }
    }

    fun getSystoleAndDiastole() = "${systole}/${diastole}"

    fun getStatus(): LimitValue {
        return if (PrefUtils.instant.typeLimitValue == Constant.ACC_AHA_2017) {
            getStatus(LimitValue.getList2017ACCAHA())
        } else {
            getStatus(LimitValue.getList2018ESCESH())
        }
    }

    fun getColorSystolic(): Int {
        return if (PrefUtils.instant.typeLimitValue == Constant.ACC_AHA_2017) {
            LimitValue.getList2017ACCAHA().map { it.sys }.toMutableList()
                .findLast { sys -> sys.contains(systole) }!!.color
        } else {
            LimitValue.getList2018ESCESH().map { it.sys }.toMutableList()
                .findLast { sys -> sys.contains(systole) }!!.color
        }
    }

    fun getColorDiastolic(): Int {
        return if (PrefUtils.instant.typeLimitValue == Constant.ACC_AHA_2017) {
            LimitValue.getList2017ACCAHA().map { it.dia }.toMutableList()
                .findLast { sys -> sys.contains(systole) }!!.color
        } else {
            LimitValue.getList2018ESCESH().map { it.dia }.toMutableList()
                .findLast { sys -> sys.contains(systole) }!!.color
        }
    }

    private fun getStatus(list: List<LimitValue>): LimitValue {
        var sysLoop = 0
        while (sysLoop <= list.size - 1) {
            val valueFirst = list[sysLoop]
            val valueSys = list[sysLoop].sys
            if (systole in valueSys!!.min..valueSys.max || (valueSys.min == 0 && systole < valueSys.max) || (valueSys.max == 0 && systole > valueSys.min)) {
                var diaLoop = sysLoop
                while (diaLoop <= list.size - 1) {
                    val valueFind = list[diaLoop]
                    val valueDia = list[diaLoop].dia
                    if (diastole in valueDia!!.min..valueDia.max || (valueDia.min == 0 && diastole < valueDia.max) || (valueDia.max == 0 && diastole > valueDia.min)) {
                        valueFind.selectedColor = valueDia.color
                        return valueFind
                    }
                    diaLoop++
                }
                valueFirst.selectedColor = valueFirst.sys!!.color
                return valueFirst
            }
            sysLoop++
        }
        return list[0].apply {
            selectedColor = sys!!.color
        }
    }

    fun getStatusRecommendName(): String {
        return when (getStatus().name) {
            R.string.lower -> "lower"
            R.string.normal -> "normal"
            R.string.high_normal -> "high_normal"
            R.string.optimal -> "optimal"
            R.string.elevated -> "elevated"
            R.string.hypertension_stage_1 -> "hypertension_stage_1"
            R.string.hypertension_stage_2 -> "hypertension_stage_2"
            else -> "hypertension_stage_3"
        }
    }
}