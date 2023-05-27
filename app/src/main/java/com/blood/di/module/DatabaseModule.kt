package com.blood.di.module

import android.content.Context
import androidx.room.Room
import com.blood.db.AppDatabase
import com.blood.db.dao.BloodPressureDAO
import com.blood.db.dao.ProfileDao
import com.blood.db.datasource.BloodPressureDataSource
import com.blood.db.datasource.ProfileDataSource
import com.blood.db.datasource.interfacedatasource.IBloodPressureDataSource
import com.blood.db.datasource.interfacedatasource.IProfileDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    companion object {
        const val DATABASE_NAME = "blood_pressure_db"
    }

    @Singleton
    @Provides
    fun getDatabase(context: Context): AppDatabase {
        val database = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)

            // Todo: If want to add default value for any table
//            .createFromAsset("cities.db")
//            .addCallback(object : RoomDatabase.Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                    db.execSQL("INSERT INTO cities_fts(cities_fts) VALUES ('rebuild')")
//                }
//            })
            .fallbackToDestructiveMigration()
            .build()
        return database
    }


    @Singleton
    @Provides
    fun provideProfileDao(db: AppDatabase): ProfileDao {
        return db.profileDao()
    }

    @Singleton
    @Provides
    fun provideProfileDataSource(profileDao: ProfileDao): IProfileDataSource {
        return ProfileDataSource(profileDao)
    }

    @Singleton
    @Provides
    fun provideBloodPressureDAO(db: AppDatabase): BloodPressureDAO {
        return db.bloodPressureDAO()
    }

    @Singleton
    @Provides
    fun provideBloodPressureDataSource(bloodPressureDAO: BloodPressureDAO): IBloodPressureDataSource {
        return BloodPressureDataSource(bloodPressureDAO)
    }
}