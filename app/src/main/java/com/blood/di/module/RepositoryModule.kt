package com.blood.di.module

import com.blood.data.repository.BloodPressureRepository
import com.blood.data.repository.ProfileRepository
import com.blood.db.datasource.interfacedatasource.IBloodPressureDataSource
import com.blood.db.datasource.interfacedatasource.IProfileDataSource
import com.blood.network.api.interfaceapi.IProfileAPI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideProfileRepository(
        iProfileDataSource: IProfileDataSource,
        iProfileAPI: IProfileAPI
    ): ProfileRepository =
        ProfileRepository(iProfileDataSource, iProfileAPI)

    @Provides
    @Singleton
    fun provideBloodPressureRepository(iBloodPressureDataSource: IBloodPressureDataSource): BloodPressureRepository =
        BloodPressureRepository(iBloodPressureDataSource)
}