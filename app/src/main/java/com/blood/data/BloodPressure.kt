package com.blood.data

import com.blood.base.BaseData
import com.blood.common.Constant
import com.blood.utils.DateUtils
import com.blood.utils.DateUtils.strDateTime
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

    fun getSystoleRange(): Int {
        return when {
            systole < 90 -> R.string.systole_range_low
            systole in 90..120 -> R.string.systole_range_normal
            systole in 120..139 -> R.string.systole_range_hypertension_stage_1
            systole in 140..179 -> R.string.systole_range_hypertension_stage_2
            else -> R.string.systole_range_emergency_care_needed
        }
    }

    fun setupDiastoleRange(): Int {
        return when {
            diastole < 60 -> R.string.diastole_range_low
            diastole in 60..80 -> R.string.diastole_range_normal_and_elevated
            diastole in 80..89 -> R.string.diastole_range_hypertension_stage_1
            diastole in 90..100 -> R.string.diastole_range_hypertension_stage_2
            else -> R.string.diastole_range_emergency_care_needed

        }
    }

    fun getStatus(): Int {
        return if (systole < 90) {
            if (diastole < 60) {
                R.string.low
            } else if (diastole < 80) {
                R.string.normal
            } else if (diastole < 90) {
                R.string.hypertension_stage_1
            } else if (diastole < 100) {
                R.string.hypertension_stage_2
            } else {
                R.string.emergency_care_needed
            }
        } else if (systole < 120) {
            if (diastole < 80) {
                R.string.normal
            } else if (diastole < 90) {
                R.string.hypertension_stage_1
            } else if (diastole < 100) {
                R.string.hypertension_stage_2
            } else {
                R.string.emergency_care_needed
            }
        } else if (systole < 140) {
            if (diastole < 90) {
                R.string.hypertension_stage_1
            } else if (diastole < 100) {
                R.string.hypertension_stage_2
            } else {
                R.string.emergency_care_needed
            }
        } else if (systole < 180) {
            if (diastole < 100) {
                R.string.hypertension_stage_2
            } else {
                R.string.emergency_care_needed
            }
        } else {
            R.string.emergency_care_needed
        }
    }

    fun getStatusColor(): Int {
        return when (getStatus()) {
            R.string.low -> R.color.color_conclusion_low
            R.string.normal -> R.color.color_conclusion_normal
            R.string.elevated -> R.color.color_conclusion_elevated
            R.string.hypertension_stage_1 -> R.color.color_conclusion_hypertension_stage_1
            R.string.hypertension_stage_2 -> R.color.color_conclusion_hypertension_stage_2
            else -> R.color.color_conclusion_emergency_care_needed
        }
    }

    fun getStatusRecommend(): Int {
        return when (getStatus()) {
            R.string.low -> R.string.recommendations_normal
            R.string.normal -> R.string.recommendations_normal
            R.string.elevated -> R.string.recommendations_normal
            R.string.hypertension_stage_1 -> R.string.recommendations_hypertension_stage_1
            R.string.hypertension_stage_2 -> R.string.recommendations_hypertension_stage_2
            else -> R.string.recommendations_emergency_care_needed
        }
    }
}