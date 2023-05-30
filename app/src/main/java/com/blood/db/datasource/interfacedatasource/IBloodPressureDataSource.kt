package com.blood.db.datasource.interfacedatasource

import com.blood.db.entity.BloodPressureEntity
import java.util.Date

interface IBloodPressureDataSource {
    suspend fun countBloodPressureByProfileID(profileId: Long): Int

    suspend fun getBloodPressureByID(id: Long): BloodPressureEntity

    suspend fun getListBloodPressure(profileId: Long): List<BloodPressureEntity>?

    suspend fun getListBloodPressureByFilterDate(
        profileId: Long, fromDate: Date? = null, toDate: Date? = null
    ): List<BloodPressureEntity>?

    suspend fun getTopBloodPressure(
        profileId: Long,
        top: Int = 0
    ): List<BloodPressureEntity>?

    suspend fun insertBloodPressure(bloodPressureEntity: BloodPressureEntity): Long

    suspend fun updateBloodPressure(bloodPressureEntity: BloodPressureEntity)

    suspend fun deleteBlood(id: Long)
}