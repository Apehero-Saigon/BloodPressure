package com.blood.db.datasource.interfacedatasource

import com.blood.db.entity.ProfileEntity

interface IProfileDataSource {

    suspend fun getProfileByID(id: Long): ProfileEntity

    suspend fun insertProfile(profile: ProfileEntity): Long
}