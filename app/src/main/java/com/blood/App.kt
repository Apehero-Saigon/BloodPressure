package com.blood

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
        lateinit var adsUtils: AdsUtils
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
        app = this
        aperoAdConfig.idAdResume = BuildConfig.appopen_resume
        aperoAdConfig.idAdResumeMedium = BuildConfig.appopen_resume_medium
        aperoAdConfig.idAdResumeHigh = BuildConfig.appopen_resume_high
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
            "5671823AE351B6E019F9664AEB1F547F",
            "5BF4DB38301AF4F36E8076F3B1D64D9D",
            "2518E22A9E93E1E7778F50C81A6AE058",
            "B0B07435EDB5A997C2E26B71A0AC1FB2"
        )
        val adjustConfig = AdjustConfig("jt22bikn5kw0")
        aperoAdConfig.adjustConfig = adjustConfig

        AdsMediationUtils.init(this, aperoAdConfig.listDeviceTest)

        Admob.getInstance().setFan(false)
        Admob.getInstance().setAppLovin(false)
        Admob.getInstance().setColony(false)
        Admob.getInstance().setOpenActivityAfterShowInterAds(true)
        Admob.getInstance().setDisableAdResumeWhenClickAds(true)

        //Firebase analytics
//        if (!BuildConfig.DEBUG) {
        FirebaseUtils.init(this)
//        }
        LanguageUtils.loadLocale(this, prefUtils.defaultLanguage)
        AperoAd.getInstance().init(this, aperoAdConfig, false)
        adsUtils = AdsUtils()
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var prefUtils: PrefUtils

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    @Suppress("DEPRECATION")
    fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }
}