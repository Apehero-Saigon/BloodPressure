package com.blood.data.repository

import com.blood.data.BloodPressure
import com.blood.data.mapping.MappingData.toBloodPressure
import com.blood.db.datasource.interfacedatasource.IBloodPressureDataSource
import com.blood.db.entity.BloodPressureEntity
import javax.inject.Inject

class BloodPressureRepository @Inject constructor(
    var iBloodPressureDataSource: IBloodPressureDataSource
) {
    suspend fun countBloodPressureByProfileID(profileId: Long): Int {
        return iBloodPressureDataSource.countBloodPressureByProfileID(profileId)
    }

    suspend fun insertBloodPressure(bloodPressure: BloodPressure): BloodPressure? {
        val id = iBloodPressureDataSource.insertBloodPressure(
            BloodPressureEntity.fromBloodPressure(bloodPressure)
        )

        if (id != -1L) {
            return iBloodPressureDataSource.getBloodPressureByID(id).toBloodPressure()
        }
        return null
    }

    suspend fun updateBloodPressure(bloodPressure: BloodPressure): BloodPressure? {
        iBloodPressureDataSource.updateBloodPressure(
            BloodPressureEntity.fromBloodPressure(
                bloodPressure
            )
        )

        return iBloodPressureDataSource.getBloodPressureByID(bloodPressure.id).toBloodPressure()
    }

    suspend fun getDetailById(id: Long): BloodPressure {
        return iBloodPressureDataSource.getBloodPressureByID(id).toBloodPressure()
    }

    suspend fun getListBloodPressureByID(profileId: Long, top: Int = 0): List<BloodPressure> {
        var listData = iBloodPressureDataSource.getListBloodPressureByProfileID(profileId, top)
        if (listData == null) {
            listData = mutableListOf()
        }

        return listData.map { it.toBloodPressure() }
    }
}