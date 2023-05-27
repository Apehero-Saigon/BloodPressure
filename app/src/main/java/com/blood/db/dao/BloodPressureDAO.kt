package com.blood.db.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BloodPressureDAO {
    @Query("SELECT COUNT(*) FROM table_blood_pressure WHERE table_blood_pressure.profileId LIKE :profileId")
    suspend fun countBloodPressureByProfileID(profileId: Long): Int
}