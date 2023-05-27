package com.blood.db.datasource.interfacedatasource

interface IBloodPressureDataSource {
    suspend fun countBloodPressureByProfileID(profileId: Long): Int
}