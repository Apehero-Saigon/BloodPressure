package com.blood

import com.ads.control.admob.Admob
import com.ads.control.admob.AppOpenManager
import com.ads.control.ads.AperoAd
import com.ads.control.application.AdsMultiDexApplication
import com.ads.control.config.AdjustConfig
import com.ads.control.config.AperoAdConfig
import com.blood.di.AppInjector
import com.blood.utils.AdsMediationUtils
import com.blood.utils.AdsUtils
import com.blood.utils.FirebaseUtils
import com.blood.utils.LanguageUtils
import com.blood.utils.PrefUtils
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : AdsMultiDexApplication(), HasAndroidInjector {
    companion object {
        lateinit var app: App
        val adsUtils: AdsUtils = AdsUtils()
    }

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        val environment = if (BuildConfig.build_debug) {
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
            "577C9208AEFF7C67F9A420B37E32681F",
            "71EE5E1FE7387B101A4E97B7455C7E8D",
            "2749DE96DA654B83523619E83F97AEF9",
            "4BD458E9A3110FE16EA87467CD87348D",
            "D8185E59A643A62B3E4930896D04E8C6",
            "3AD81E5878CEF2A2502706477E817FBC",
            "1AB1217C2EE935D73D24939FCF65AF1C",
            "545F8B1A7D7A0C52C11A84EDF0D6A4C1",
            "2EA654B2658398843F0BF4D3988AA07E",
            "03D93D429D0EF80E1AFDC351F3D1209A",
            "D340AD4684CEFC620FB6F45C55740859",
            "CE9E94B617BEA63E8479B8302B2210B8",
            "74521D8D41B7B0A844EC9972E711421D",
            "13ED83446977FB9F92089E5E84B97496",
            "B0B07435EDB5A997C2E26B71A0AC1FB2"
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
        LanguageUtils.loadLocale(this, prefUtils.defaultLanguage)
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var prefUtils: PrefUtils

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}