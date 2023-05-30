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

    fun getSystoleAndDiastole() = "${systole}/${diastole}"

    fun getStatus(): LimitValue {
        return if (PrefUtils.instant.typeLimitValue == Constant.ACC_AHA_2017) {
            getStatus(LimitValue.getList2017ACCAHA())
        } else {
            getStatus(LimitValue.getList2018ESCESH())
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
                if (!valueFirst.sameValueSys) {
                    valueFirst.selectedColor = valueFirst.sys!!.color
                    return valueFirst
                }
            }
            sysLoop++
        }
        return list[0].apply {
            selectedColor = sys!!.color
        }
    }

    fun getStatusRecommend(): Int {
        return when (getStatus().name) {
            R.string.lower -> R.string.recommendations_normal
            R.string.normal -> R.string.recommendations_normal
            R.string.high_normal -> R.string.recommendations_normal
            R.string.optimal -> R.string.recommendations_normal
            R.string.elevated -> R.string.recommendations_normal
            R.string.hypertension_stage_1 -> R.string.recommendations_hypertension_stage_1
            R.string.hypertension_stage_2 -> R.string.recommendations_hypertension_stage_2
            else -> R.string.recommendations_hypertension_stage_3
        }
    }
}