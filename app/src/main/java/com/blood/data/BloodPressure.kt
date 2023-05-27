package com.blood.data

import java.io.Serializable
import java.util.Date

data class BloodPressure(
    var id: Long = 0,
    var profileId: Long,
    var systole: Int,
    var diastole: Int,
    var pulse: Int,
    var createAt: Date,
) : Serializable