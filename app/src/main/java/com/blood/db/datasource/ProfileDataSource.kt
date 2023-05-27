package com.blood.db.datasource

import com.blood.data.Profile
import com.blood.db.dao.ProfileDao
import com.blood.db.datasource.interfacedatasource.IProfileDataSource
import com.blood.db.entity.ProfileEntity
import javax.inject.Inject

class ProfileDataSource
@Inject constructor(private val profileDao: ProfileDao) : IProfileDataSource {
    override suspend fun getProfileByID(id: Long): ProfileEntity {
        return profileDao.getProfileByID(id)
    }

    override suspend fun insertProfile(profile: ProfileEntity): Long {
        return profileDao.insertProfile(profile)
    }
}