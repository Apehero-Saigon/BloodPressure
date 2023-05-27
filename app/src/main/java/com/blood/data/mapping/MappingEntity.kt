package com.blood.data.mapping

import com.blood.data.Profile
import com.blood.db.entity.ProfileEntity
import kotlin.reflect.full.memberProperties

object MappingEntity {
    fun Profile.toProfileEntity() = with(::ProfileEntity) {
        val propertiesByName = Profile::class.memberProperties.associateBy { it.name }
        callBy(parameters.associate { parameter ->
            parameter to when (parameter.name) {
                Profile::id.name -> name
                Profile::birthYear.name -> birthYear
                Profile::height.name -> height
                Profile::weight.name -> weight
                else -> propertiesByName[parameter.name]?.get(this@toProfileEntity)
            }
        })
    }
}