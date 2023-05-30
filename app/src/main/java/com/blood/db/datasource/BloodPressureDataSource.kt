package com.blood.db.datasource

import com.blood.db.dao.BloodPressureDAO
import com.blood.db.datasource.interfacedatasource.IBloodPressureDataSource
import com.blood.db.entity.BloodPressureEntity
import java.util.Date
import javax.inject.Inject

class BloodPressureDataSource @Inject constructor(private val bloodPressureDAO: BloodPressureDAO) :
    IBloodPressureDataSource {
    override suspend fun countBloodPressureByProfileID(profileId: Long): Int {
        return bloodPressureDAO.countBloodPressureByProfileID(profileId)
    }

    override suspend fun getBloodPressureByID(id: Long): BloodPressureEntity {
        return bloodPressureDAO.getBloodPressureByID(id)
    }

    override suspend fun getListBloodPressure(profileId: Long): List<BloodPressureEntity>? {
        return bloodPressureDAO.getAllBloodPressure(profileId)
    }

    override suspend fun getListBloodPressureByFilterDate(
        profileId: Long, fromDate: Date?, toDate: Date?
    ): List<BloodPressureEntity>? {
        return bloodPressureDAO.getBloodPressureFilterDate(profileId, fromDate, toDate)
    }

    override suspend fun getTopBloodPressure(
        profileId: Long, top: Int
    ): List<BloodPressureEntity>? {
        return bloodPressureDAO.getTopBloodPressure(profileId, top)
    }

    override suspend fun updateBloodPressure(bloodPressureEntity: BloodPressureEntity) {
        bloodPressureDAO.updateBloodPressure(bloodPressureEntity)
    }

    override suspend fun deleteBlood(id: Long) {
        bloodPressureDAO.deleteBloodPressure(id)
    }

    override suspend fun insertBloodPressure(bloodPressureEntity: BloodPressureEntity): Long {
        return bloodPressureDAO.insertBloodPressure(bloodPressureEntity)
    }
}