package com.blood.data.mapping

import com.blood.data.Profile
import com.blood.db.entity.ProfileEntity
import kotlin.reflect.full.memberProperties

object MappingData {
    fun ProfileEntity.toProfile() = with(::Profile) {
        val propertiesByName = ProfileEntity::class.memberProperties.associateBy { it.name }
        callBy(parameters.associate { parameter ->
            parameter to when (parameter.name) {
                ProfileEntity::id.name -> id
                ProfileEntity::birthYear.name -> birthYear
                ProfileEntity::height.name -> height
                ProfileEntity::weight.name -> weight
                else -> propertiesByName[parameter.name]?.get(this@toProfile)
            }
        })
    }
}