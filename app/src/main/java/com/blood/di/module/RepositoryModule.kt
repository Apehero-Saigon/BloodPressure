package com.blood.di.module

import com.blood.data.repository.GlucoseRepository
import com.blood.data.repository.ProfileRepository
import com.blood.db.datasource.interfacedatasource.IGlucoseDataSource
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
    fun provideGlucoseRepository(iGlucoseDataSource: IGlucoseDataSource): GlucoseRepository =
        GlucoseRepository(iGlucoseDataSource)
}