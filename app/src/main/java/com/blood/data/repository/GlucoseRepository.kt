package com.blood.data.repository

import com.blood.db.datasource.interfacedatasource.IBloodPressureDataSource
import javax.inject.Inject

class BloodPressureRepository @Inject constructor(
    var iBloodPressureDataSource: IBloodPressureDataSource
) {
    suspend fun countBloodPressureByProfileID(profileId: Long): Int {
        return iBloodPressureDataSource.countBloodPressureByProfileID(profileId)
    }
}