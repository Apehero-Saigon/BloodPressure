package com.blood.db.datasource.interfacedatasource

import com.blood.db.entity.BloodPressureEntity

interface IBloodPressureDataSource {
    suspend fun countBloodPressureByProfileID(profileId: Long): Int

    suspend fun getBloodPressureByID(id: Long): BloodPressureEntity

    suspend fun getListBloodPressureByProfileID(
        profileId: Long, top: Int = 0
    ): List<BloodPressureEntity>?

    suspend fun insertBloodPressure(bloodPressureEntity: BloodPressureEntity): Long

    suspend fun updateBloodPressure(bloodPressureEntity: BloodPressureEntity)

}