package com.blood.db.datasource

import com.blood.db.dao.BloodPressureDAO
import com.blood.db.datasource.interfacedatasource.IBloodPressureDataSource
import javax.inject.Inject

class BloodPressureDataSource @Inject constructor(private val bloodPressureDAO: BloodPressureDAO) :
    IBloodPressureDataSource {
    override suspend fun countBloodPressureByProfileID(profileId: Long): Int {
        return bloodPressureDAO.countBloodPressureByProfileID(profileId)
    }
}