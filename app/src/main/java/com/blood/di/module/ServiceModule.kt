package com.blood.di.module

import com.blood.ui.services.ServiceManager
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract fun serviceManager(): ServiceManager
}