package com.blood.data.repository

import com.blood.data.Profile
import com.blood.data.mapping.MappingData.toProfile
import com.blood.db.datasource.interfacedatasource.IProfileDataSource
import com.blood.db.entity.ProfileEntity
import com.blood.network.api.interfaceapi.IProfileAPI
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    var profileDataSource: IProfileDataSource, var profileAPI: IProfileAPI
) {

    suspend fun insertProfile(profile: Profile): Profile? {
        val id = profileDataSource.insertProfile(ProfileEntity.fromProfile(profile))

        if (id != -1L) {
            return profileDataSource.getProfileByID(id).toProfile()
        }
        return null
    }
}