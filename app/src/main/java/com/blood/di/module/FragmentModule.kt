package com.blood.di.module

import com.blood.ui.fragments.bloodpressure.BloodPressureDetailFragment
import com.blood.ui.fragments.bloodpressure.BloodPressureEditFragment
import com.blood.ui.fragments.dashboard.DashBoardFragment
import com.blood.ui.fragments.home.HomeFragment
import com.blood.ui.fragments.info.InfoDetailFragment
import com.blood.ui.fragments.info.InfoFragment
import com.blood.ui.fragments.insight.InsightBloodPressureFragment
import com.blood.ui.fragments.insight.InsightFragment
import com.blood.ui.fragments.language.LanguageFragment
import com.blood.ui.fragments.language.LanguageSettingFragment
import com.blood.ui.fragments.limitvalues.LimitValuesFragment
import com.blood.ui.fragments.onboarding.OnBoardingFragment
import com.blood.ui.fragments.onboarding.OnBoardingPageFragment
import com.blood.ui.fragments.profile.ProfileEditFragment
import com.blood.ui.fragments.reference.DisclaimerFragment
import com.blood.ui.fragments.setting.SettingFragment
import com.blood.ui.fragments.splash.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun homeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun splashFragment(): SplashFragment

    @ContributesAndroidInjector
    abstract fun languageFragment(): LanguageFragment

    @ContributesAndroidInjector
    abstract fun onBoardingFragment(): OnBoardingFragment

    @ContributesAndroidInjector
    abstract fun onBoardingPageFragment(): OnBoardingPageFragment

    @ContributesAndroidInjector
    abstract fun profileEditFragment(): ProfileEditFragment

    @ContributesAndroidInjector
    abstract fun dashBoardFragment(): DashBoardFragment

    @ContributesAndroidInjector
    abstract fun insightFragment(): InsightFragment

    @ContributesAndroidInjector
    abstract fun InsightBloodPressureFragment(): InsightBloodPressureFragment

    @ContributesAndroidInjector
    abstract fun infoFragment(): InfoFragment

    @ContributesAndroidInjector
    abstract fun infoDetailFragment(): InfoDetailFragment

    @ContributesAndroidInjector
    abstract fun settingFragment(): SettingFragment

    @ContributesAndroidInjector
    abstract fun disclaimerFragment(): DisclaimerFragment

    @ContributesAndroidInjector
    abstract fun languageSettingFragment(): LanguageSettingFragment

    @ContributesAndroidInjector
    abstract fun bloodPressureDetailFragment(): BloodPressureDetailFragment

    @ContributesAndroidInjector
    abstract fun bloodPressureEditFragment(): BloodPressureEditFragment

    @ContributesAndroidInjector
    abstract fun limitValuesFragment(): LimitValuesFragment
}