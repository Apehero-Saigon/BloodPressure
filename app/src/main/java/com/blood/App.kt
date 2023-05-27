package com.blood

import com.ads.control.admob.Admob
import com.ads.control.admob.AppOpenManager
import com.ads.control.ads.AperoAd
import com.ads.control.application.AdsMultiDexApplication
import com.ads.control.config.AdjustConfig
import com.ads.control.config.AperoAdConfig
import com.blood.utils.AdsUtils
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.blood.di.AppInjector
import com.blood.utils.AdsMediationUtils
import com.blood.utils.FirebaseUtils
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : AdsMultiDexApplication(), HasAndroidInjector {
    companion object {
        lateinit var app: App
        val adsUtils : AdsUtils = AdsUtils()
    }

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        val environment = if (BuildConfig.ENV_TEST) {
            AperoAdConfig.ENVIRONMENT_DEVELOP
        } else {
            AperoAdConfig.ENVIRONMENT_PRODUCTION
        }
        aperoAdConfig = AperoAdConfig(this, AperoAdConfig.PROVIDER_ADMOB, environment)
        aperoAdConfig.mediationProvider = AperoAdConfig.PROVIDER_ADMOB
        AppOpenManager.getInstance().setSplashAdId(BuildConfig.appopen_splash)
        app = this
        aperoAdConfig.idAdResume = BuildConfig.appopen_resume
        aperoAdConfig.listDeviceTest = listOf(
            "577C9208AEFF7C67F9A420B37E32681F"
        )
        listTestDevice.add("577C9208AEFF7C67F9A420B37E32681F")
        aperoAdConfig.listDeviceTest = listTestDevice

        val adjustConfig = AdjustConfig("jt22bikn5kw0")
        aperoAdConfig.adjustConfig = adjustConfig

        AdsMediationUtils.init(this, aperoAdConfig.listDeviceTest)
        AperoAd.getInstance().init(this, aperoAdConfig, false)

        Admob.getInstance().setFan(false)
        Admob.getInstance().setAppLovin(false)
        Admob.getInstance().setColony(false)
        Admob.getInstance().setOpenActivityAfterShowInterAds(true)
        Admob.getInstance().setDisableAdResumeWhenClickAds(true)

        //Firebase analytics
        FirebaseUtils.init(this)
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}