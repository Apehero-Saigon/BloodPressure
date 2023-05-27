package com.blood.di.module

import com.blood.di.scope.PerActivity
import com.blood.ui.activities.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @PerActivity
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun mainActivity(): MainActivity
}