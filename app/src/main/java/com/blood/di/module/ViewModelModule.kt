package com.blood.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blood.base.BaseViewModel
import com.blood.di.ViewModelFactory
import com.blood.di.key.ViewModelKey
import com.blood.ui.fragments.bloodpressure.BloodPressureViewModel
import com.blood.ui.fragments.home.HomeViewModel
import com.blood.ui.fragments.insight.InsightViewModel
import com.blood.ui.fragments.profile.ProfileViewModel
import com.blood.ui.fragments.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(BaseViewModel::class)
    abstract fun provideBaseViewModel(baseViewModel: BaseViewModel): ViewModel


    @IntoMap
    @Binds
    @ViewModelKey(HomeViewModel::class)
    abstract fun provideHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(SplashViewModel::class)
    abstract fun provideSplashViewModel(splashViewModel: SplashViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(ProfileViewModel::class)
    abstract fun provideProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(BloodPressureViewModel::class)
    abstract fun provideBloodPressureViewModel(bloodPressureViewModel: BloodPressureViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(InsightViewModel::class)
    abstract fun provideInsightViewModel(insightViewModel: InsightViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}