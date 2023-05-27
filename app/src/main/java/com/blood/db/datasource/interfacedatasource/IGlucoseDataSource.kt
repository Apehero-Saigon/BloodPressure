package com.blood.db.datasource.interfacedatasource

interface IGlucoseDataSource {
    suspend fun countBloodSugarByProfileID(profileId: Long): Int
}