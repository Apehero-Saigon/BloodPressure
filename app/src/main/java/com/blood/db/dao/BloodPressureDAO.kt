package com.blood.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.blood.db.entity.BloodPressureEntity
import java.util.Date

@Dao
interface BloodPressureDAO {
    @Query("SELECT COUNT(*) FROM table_blood_pressure WHERE table_blood_pressure.profileId LIKE :profileId")
    suspend fun countBloodPressureByProfileID(profileId: Long): Int

    @Query("SELECT * FROM table_blood_pressure WHERE id =:id")
    suspend fun getBloodPressureByID(id: Long): BloodPressureEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBloodPressure(bloodPressureEntity: BloodPressureEntity): Long

    @Query("SELECT * FROM table_blood_pressure WHERE profileId LIKE :profileId ORDER BY id DESC")
    suspend fun getAllBloodPressure(profileId: Long): List<BloodPressureEntity>?

    @Query("SELECT * FROM table_blood_pressure WHERE profileId LIKE :profileId ORDER BY id DESC LIMIT :top")
    suspend fun getTopBloodPressure(profileId: Long, top: Int): List<BloodPressureEntity>?

    @Query("SELECT * FROM table_blood_pressure WHERE profileId LIKE :profileId AND createdAt BETWEEN :fromData AND :toData ORDER BY id DESC")
    suspend fun getBloodPressureFilterDate(
        profileId: Long, fromData: Date?, toData: Date?
    ): List<BloodPressureEntity>?

    @Update
    suspend fun updateBloodPressure(bloodPressureEntity: BloodPressureEntity)

    @Query("DELETE FROM table_blood_pressure WHERE id=:id")
    suspend fun deleteBloodPressure(id: Long)
}