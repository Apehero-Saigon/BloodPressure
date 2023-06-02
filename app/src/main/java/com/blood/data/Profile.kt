package com.blood.data

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Profile(
    var id: Long = 0,
    var birthYear: Int = 0,
    var gender: Int = 0,
    var height: Int = 0,
    var weight: Int = 0
) : Serializable
