package com.blood.ui.fragments.splash

import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ads.control.admob.AppOpenManager
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.ads.control.ads.wrapper.ApAdError
import com.ads.control.ads.wrapper.ApInterstitialAd
import com.ads.control.funtion.AdCallback
import com.blood.App
import com.blood.base.BaseActivity
import com.blood.base.BaseFragment
import com.blood.common.Constant
import com.blood.utils.AdsUtils
import com.blood.utils.FirebaseUtils
import com.blood.utils.PrefUtils
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentSplashBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>(
    R.layout.fragment_splash, SplashViewModel::class.java
) {

    companion object {
        private enum class SplashType { INTERSTITIAL, FAIL }

        private enum class FetchRemoteConfig { FETCHING, DONE, NONE }
    }

    private var splashLoadType: SplashType? = null
    private var fetchRemoteConfig: FetchRemoteConfig = FetchRemoteConfig.NONE
    private var errorShowAdsInBackground: Boolean = false
    private var finishedSplash = false

    override fun initAds() {
        adsUtils.nativeExit.loadAds(requireActivity())

        if (prefUtils.isShowLanguageFirstOpen) {
            adsUtils.nativeLanguage.loadAds(requireActivity())
        }


        if (prefUtils.isShowOnBoardingFirstOpen) {
            App.adsUtils.nativeOnBoarding.loadAds(requireActivity())
        }

        if (!prefUtils.isShowLanguageFirstOpen && !prefUtils.isShowOnBoardingFirstOpen) {
            App.adsUtils.nativeBloodPressure.loadAds(requireActivity())
        }

        adsUtils.banner.loadAds(requireActivity())
    }

    override fun initData() {
        AppOpenManager.getInstance().disableAppResume()
        FirebaseUtils.eventSplashScreen()
        if (openByChangeLanguage()) {
            val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
            safeNav(action)
        }
    }

    private fun setupRemoteConfig() {
        fetchRemoteConfig = FetchRemoteConfig.FETCHING
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder().setFetchTimeoutInSeconds(5000).setMinimumFetchIntervalInSeconds(3600).build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                fetchDataRemote(firebaseRemoteConfig)
            }
            fetchRemoteConfig = FetchRemoteConfig.DONE
            loadAds()
        }.addOnFailureListener {
            fetchRemoteConfig = FetchRemoteConfig.DONE
            loadAds()
        }
    }

    private fun fetchDataRemote(firebaseRemoteConfig: FirebaseRemoteConfig) {
        prefUtils.isShowAppOpenResume = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_APPOPEN_RESUME)
        prefUtils.isShowAppOpenSplash = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_APPOPEN_SPLASH)
        prefUtils.isShowBannerHome = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_BANNER_HOME)
        prefUtils.isShowInterBloodPressureDetails = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_BLOODPRESSURE_DETAILS)
        prefUtils.isShowInterBloodPressureDetailsHigh = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_BLOODPRESSURE_DETAILS_HIGH)
        prefUtils.isShowInterInfo = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_INFO)
        prefUtils.isShowInterInfoMedium = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_INFO_MEDIUM)
        prefUtils.isShowInterInfoHigh = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_INFO_HIGH)
        prefUtils.isShowInterMeasure = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_MEASURE)
        prefUtils.isShowInterMeasureHigh = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_MEASURE_HIGH)
        prefUtils.isShowInterSave = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_SAVE)
        prefUtils.isShowInterSaveMedium = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_SAVE_MEDIUM)
        prefUtils.isShowInterSaveHigh = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_SAVE_HIGH)
        prefUtils.isShowInterSplash = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_SPLASH)
        prefUtils.isShowInterInsightDetailHigh = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_INSIGHT_DETAILS_HIGH)
        prefUtils.isShowInterInsightDetailMedium = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_INSIGHT_DETAILS_MEDIUM)
        prefUtils.isShowInterInsightDetail = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_INSIGHT_DETAILS)
        prefUtils.isShowNativeLanguage = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_LANGUAGE)
        prefUtils.isShowNativeLanguageMedium = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_LANGUAGE_MEDIUM)
        prefUtils.isShowNativeLanguageHigh = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_LANGUAGE_HIGH)
        prefUtils.isShowNativeOnBoarding = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_ONBOARDING)
        prefUtils.isShowNativeOnBoardingMedium = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_ONBOARDING_MEDIUM)
        prefUtils.isShowNativeOnBoardingHigh = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_ONBOARDING_HIGH)
        prefUtils.isShowNativeRecentAction = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_RECENT_ACTION)
        prefUtils.isShowNativeBloodPressure = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_BLOOD_PRESSURE)
        prefUtils.isShowNativeCreateUser = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_CREATE_USER)
        prefUtils.isShowNativeDefaultValue = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_VALUE)
        prefUtils.isShowNativeExit = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_EXIT)
        prefUtils.isShowNativeExitMedium = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_EXIT_MEDIUM)
        prefUtils.isShowNativeExitHigh = firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_EXIT_HIGH)
    }

    private fun onShowSplashScreen() {
        if (fetchRemoteConfig == FetchRemoteConfig.DONE) {
            AperoAd.getInstance().onShowSplashPriority3(requireActivity() as AppCompatActivity?, object : AperoAdCallback() {
                override fun onNextAction() {
                    super.onNextAction()
                    goToMainScreen()
                }

                override fun onAdFailedToShow(adError: ApAdError?) {
                    super.onAdFailedToShow(adError)
                    if ((requireActivity() as? BaseActivity<*, *>)?.isOnResume == false) {
                        errorShowAdsInBackground = true
                    }
                    goToMainScreen()
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    goToMainScreen()
                }
            })
        } else {
            goToMainScreen()
        }
    }

    private fun loadAds() {
        if (isNetworkConnected()) {
            AperoAd.getInstance().loadSplashInterPriority3SameTime(
                requireContext(),
                BuildConfig.inter_splash_high,
                BuildConfig.inter_splash_medium,
                BuildConfig.inter_splash,
                30000,
                5000,
                false,
                object : AperoAdCallback() {

                    override fun onAdFailedToLoad(adError: ApAdError?) {
                        super.onAdFailedToLoad(adError)
                        splashLoadType = SplashType.FAIL
                        goToMainScreen()
                    }

                    override fun onAdSplashReady() {
                        super.onAdSplashReady()
                        splashLoadType = SplashType.INTERSTITIAL
                        onShowSplashScreen()
                    }

                    override fun onAdSplashPriorityReady() {
                        super.onAdSplashPriorityReady()
                        splashLoadType = SplashType.INTERSTITIAL
                        onShowSplashScreen()
                    }

                    override fun onAdSplashPriorityMediumReady() {
                        super.onAdSplashPriorityMediumReady()
                        splashLoadType = SplashType.INTERSTITIAL
                        onShowSplashScreen()
                    }

                    override fun onNextAction() {
                        super.onNextAction()
                        goToMainScreen()
                    }
                }
            )
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                goToMainScreen()
            }, 2000)
        }
    }

    override fun onResume() {
        super.onResume()
        AppOpenManager.getInstance().disableAppResume()

        if (errorShowAdsInBackground) {
            errorShowAdsInBackground = false
            if (isNetworkConnected()) {
                onShowSplashScreen()
            } else {
                goToMainScreen()
            }
            return
        }

        // check conditional
        if (fetchRemoteConfig == FetchRemoteConfig.NONE && !openByChangeLanguage()) {
            setupRemoteConfig()
        }

        //if go onResume but not have action
        if (!finishedSplash) {
            if (splashLoadType == SplashType.INTERSTITIAL) {
                AperoAd.getInstance().onCheckShowSplashPriority3WhenFail(
                    requireActivity() as AppCompatActivity, object : AperoAdCallback() {
                        override fun onNextAction() {
                            super.onNextAction()
                            goToMainScreen()
                        }

                        override fun onAdClosed() {
                            super.onAdClosed()
                            goToMainScreen()
                        }

                        override fun onAdFailedToShow(adError: ApAdError?) {
                            super.onAdFailedToShow(adError)
                            goToMainScreen()
                        }

                    }, 1000
                )
            }
        }
    }

    private fun goToMainScreen() {
        if (!finishedSplash) {
            if ((requireActivity() as? BaseActivity<*, *>)?.isOnResume == false) {
                errorShowAdsInBackground = true
            }
            if (prefUtils.isShowLanguageFirstOpen) {
                val action = SplashFragmentDirections.actionSplashFragmentToLanguageFragment()
                safeNav(action)
            } else {
                if (prefUtils.isShowOnBoardingFirstOpen) {
                    val action = SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment()
                    safeNav(action)
                }
//                else if (prefUtils.profile == null) {
//                    val action =
//                        SplashFragmentDirections.actionSplashFragmentToProfileEditFragment()
//                    action.editMode = false
//                    safeNav(action)
//                } else if (prefUtils.typeLimitValue.isEmpty()) {
//                    val action =
//                        SplashFragmentDirections.actionSplashFragmentToMeasurementGuidelineDefaultFragment()
//                    safeNav(action)
//                }
                else {
                    val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                    safeNav(action)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        AppOpenManager.getInstance().enableAppResume()
        finishedSplash = true
    }

    private fun openByChangeLanguage(): Boolean {
        if (requireActivity().intent.hasExtra(Constant.KEY_START_SPLASH_FROM)) {
            return requireActivity().intent.getBooleanExtra(Constant.KEY_START_SPLASH_FROM, false)
        }
        return false
    }
}