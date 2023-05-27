package com.blood.db.datasource

import com.blood.db.dao.GlucoseDAO
import com.blood.db.datasource.interfacedatasource.IGlucoseDataSource
import javax.inject.Inject

class GlucoseDataSource @Inject constructor(private val glucoseDAO: GlucoseDAO) :
    IGlucoseDataSource {
    override suspend fun countBloodSugarByProfileID(profileId: Long): Int {
        return glucoseDAO.countBloodSugarByProfileID(profileId)
    }
}