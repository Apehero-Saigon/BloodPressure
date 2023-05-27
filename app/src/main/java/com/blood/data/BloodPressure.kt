package com.blood.data

import java.io.Serializable
import java.util.Date

data class BloodPressure(
    var profileId: Long,
    var systole: Int,
    var diastole: Int,
    var pulse: Int = 0,
    var createAt: Date?,
    var note: String,
    var id: Long = 0
) : Serializable