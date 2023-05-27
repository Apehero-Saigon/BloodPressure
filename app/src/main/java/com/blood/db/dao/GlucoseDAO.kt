package com.blood.db.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface GlucoseDAO {
    @Query("SELECT COUNT(*) FROM glucose WHERE glucose.profileId LIKE :profileId")
    suspend fun countBloodSugarByProfileID(profileId: Long): Int
}