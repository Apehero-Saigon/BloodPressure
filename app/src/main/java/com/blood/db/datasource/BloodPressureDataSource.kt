package com.blood.db.datasource

import com.blood.db.dao.BloodPressureDAO
import com.blood.db.datasource.interfacedatasource.IBloodPressureDataSource
import com.blood.db.entity.BloodPressureEntity
import javax.inject.Inject

class BloodPressureDataSource @Inject constructor(private val bloodPressureDAO: BloodPressureDAO) :
    IBloodPressureDataSource {
    override suspend fun countBloodPressureByProfileID(profileId: Long): Int {
        return bloodPressureDAO.countBloodPressureByProfileID(profileId)
    }

    override suspend fun getBloodPressureByID(id: Long): BloodPressureEntity {
        return bloodPressureDAO.getBloodPressureByID(id)
    }

    override suspend fun insertBloodPressure(bloodPressureEntity: BloodPressureEntity): Long {
        return bloodPressureDAO.insertBloodPressure(bloodPressureEntity)
    }
}