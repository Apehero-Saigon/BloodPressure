package com.blood.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.blood.db.converter.Converters
import com.blood.db.dao.BloodPressureDAO
import com.blood.db.dao.ProfileDao
import com.blood.db.entity.BloodPressureEntity
import com.blood.db.entity.ProfileEntity

@TypeConverters(Converters::class)
@Database(entities = [ProfileEntity::class, BloodPressureEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao

    abstract fun bloodPressureDAO(): BloodPressureDAO
}