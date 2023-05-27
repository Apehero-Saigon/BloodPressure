package com.blood.data

import java.io.Serializable

data class Profile(
    var id: Long = 0,
    var name: String?,
    var birthYear: Int?,
    var gender: Int?,
    var height: Int?,
    var weight: Int?
) : Serializable
