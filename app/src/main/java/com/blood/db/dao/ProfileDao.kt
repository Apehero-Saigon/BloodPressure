package com.blood.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.blood.db.entity.ProfileEntity

@Dao
interface ProfileDao {
    @Query("SELECT * FROM table_profile WHERE id =:id")
    suspend fun getProfileByID(id: Long): ProfileEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProfile(profile: ProfileEntity): Long
}