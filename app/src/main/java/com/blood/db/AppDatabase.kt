package com.blood.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.blood.db.converter.StringConverters
import com.blood.db.dao.GlucoseDAO
import com.blood.db.dao.ProfileDao
import com.blood.db.entity.GlucoseEntity
import com.blood.db.entity.ProfileEntity

@TypeConverters(StringConverters::class)
@Database(entities = [ProfileEntity::class, GlucoseEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao

    abstract fun glucoseDAO(): GlucoseDAO
}