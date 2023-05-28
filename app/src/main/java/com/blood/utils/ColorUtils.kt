package com.blood.utils

import com.bloodpressure.pressuremonitor.bloodpressuretracker.R

class ColorUtils {
    companion object {
        @JvmStatic
        fun getColorSystolic(systole: Int): Int {
            return if (systole < 90) {
                R.color.color_conclusion_low
            } else if (systole < 120) {
                R.color.color_conclusion_normal
            } else if (systole < 140) {
                R.color.color_conclusion_hypertension_stage_1
            } else if (systole < 180) {
                R.color.color_conclusion_hypertension_stage_2
            } else {
                R.color.color_conclusion_emergency_care_needed
            }
        }

        @JvmStatic
        fun getColorDiastolic(systole: Int): Int {
            return if (systole < 60) {
                R.color.color_conclusion_low
            } else if (systole < 80) {
                R.color.color_conclusion_normal
            } else if (systole < 90) {
                R.color.color_conclusion_hypertension_stage_1
            } else if (systole < 100) {
                R.color.color_conclusion_hypertension_stage_2
            } else {
                R.color.color_conclusion_emergency_care_needed
            }
        }
    }
}