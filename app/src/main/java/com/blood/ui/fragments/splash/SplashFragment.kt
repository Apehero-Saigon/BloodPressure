package com.blood.ui.fragments.splash

import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
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
import com.blood.utils.PrefUtils
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentSplashBinding
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdError
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>(
    R.layout.fragment_splash, SplashViewModel::class.java
) {

    companion object {
        private enum class SplashType { OPEN_SPLASH, INTERSTITIAL, FAIL }

        private enum class FetchRemoteConfig { FETCHING, DONE, NONE }
    }

    private var splashLoadType: SplashType? = null
    private var fetchRemoteConfig: FetchRemoteConfig = FetchRemoteConfig.NONE
    private var errorShowAdsInBackground: Boolean = false
    private var finishedSplash = false

    override fun initAds() {
        if (openByChangeLanguage()) return

        if (prefUtils.isShowNativeLanguage && prefUtils.isShowLanguageFirstOpen) {
            adsUtils.nativeLanguage.loadAds(
                requireActivity(), BuildConfig.native_language, null, R.layout.native_medium
            )
        }


        if (isNetworkConnected() && prefUtils.isShowOnBoardingFirstOpen && prefUtils.isShowNativeOnBoarding) {
            App.adsUtils.nativeOnBoarding1.loadAds(
                requireActivity(),
                BuildConfig.native_onboarding,
                null,
                R.layout.layout_native_control
            )
            App.adsUtils.nativeOnBoarding2.loadAds(
                requireActivity(),
                BuildConfig.native_onboarding,
                null,
                R.layout.layout_native_control
            )
            App.adsUtils.nativeOnBoarding3.loadAds(
                requireActivity(),
                BuildConfig.native_onboarding,
                null,
                R.layout.layout_native_control
            )
        }

        if (!prefUtils.isShowLanguageFirstOpen && !prefUtils.isShowOnBoardingFirstOpen && prefUtils.profile == null) {
            App.adsUtils.interSaveProfile.isShowHighAds = prefUtils.isShowInterSaveHigh
            App.adsUtils.interSaveProfile.isShowNormalAds = prefUtils.isShowInterSave
            App.adsUtils.interSaveProfile.loadInterPrioritySameTime(
                requireContext(), BuildConfig.inter_save_high, BuildConfig.inter_save
            )
        }
    }

    override fun initData() {
        if (openByChangeLanguage()) {
            val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
            findNavController().navigate(action)
        } else {
            Glide.with(this).load(R.drawable.img_heart_beat).into(binding.ivLogo)
        }
    }

    private fun setupRemoteConfig() {
        fetchRemoteConfig = FetchRemoteConfig.FETCHING
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder().setFetchTimeoutInSeconds(5000)
            .setMinimumFetchIntervalInSeconds(if (BuildConfig.build_debug) 3600 else 0).build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                val updated: Boolean = task.result
                if (updated) {
                    fetchDataRemote(firebaseRemoteConfig)
                }
            }
            fetchRemoteConfig = FetchRemoteConfig.DONE
            onShowSplashScreen()
        }.addOnFailureListener {
            fetchRemoteConfig = FetchRemoteConfig.DONE
            onShowSplashScreen()
        }
        loadSplash()
    }

    private fun fetchDataRemote(firebaseRemoteConfig: FirebaseRemoteConfig) {
        prefUtils.isShowAppOpenResume =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_APPOPEN_RESUME)
        prefUtils.isShowAppOpenSplash =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_APPOPEN_SPLASH)
        prefUtils.isShowBannerHome =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_BANNER_HOME)
        prefUtils.isShowInterBloodPressureDetails =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_BLOODPRESSURE_DETAILS)
        prefUtils.isShowInterBloodPressureDetailsHigh =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_BLOODPRESSURE_DETAILS_HIGH)
        prefUtils.isShowInterInfo =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_INFO)
        prefUtils.isShowInterInfoHigh =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_INFO_HIGH)
        prefUtils.isShowInterMeasure =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_MEASURE)
        prefUtils.isShowInterMeasureHigh =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_MEASURE_HIGH)
        prefUtils.isShowInterSave =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_SAVE)
        prefUtils.isShowInterSaveHigh =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_SAVE_HIGH)
        prefUtils.isShowInterSplash =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_SPLASH)
        prefUtils.isShowInterSplash =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_INTER_SPLASH)
        prefUtils.isShowNativeLanguage =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_LANGUAGE)
        prefUtils.isShowNativeOnBoarding =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_ONBOARDING)
        prefUtils.isShowNativeRecentAction =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_RECENT_ACTION)
        prefUtils.isShowNativeBloodPressure =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_NATIVE_BLOOD_PRESSURE)
        prefUtils.isShowBannerCreateUser =
            firebaseRemoteConfig.getBoolean(PrefUtils.REMOTE_SHOW_BANNER_CREATE_USER)
    }

    private fun onShowSplashScreen() {
        if (fetchRemoteConfig == FetchRemoteConfig.DONE) {
            if (SplashType.OPEN_SPLASH == splashLoadType) {
                AppOpenManager.getInstance().showAppOpenSplash(
                    requireActivity() as AppCompatActivity?,
                    object : AdCallback() {
                        override fun onNextAction() {
                            super.onNextAction()
                            goToMainScreen()
                        }

                        override fun onAdFailedToShow(adError: AdError?) {
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
            } else if (SplashType.INTERSTITIAL == splashLoadType) {
                AperoAd.getInstance().onShowSplash(
                    requireActivity() as AppCompatActivity,
                    object : AperoAdCallback() {
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
            } else if (SplashType.FAIL == splashLoadType) {
                goToMainScreen()
            }
        }
    }

    private fun loadSplash() {
        if (isNetworkConnected()) {
            AperoAd.getInstance().loadAppOpenSplashSameTime(requireContext(),
                BuildConfig.inter_splash,
                30000,
                5000,
                false,
                object : AperoAdCallback() {
                    override fun onAdFailedToLoad(adError: ApAdError?) {
                        super.onAdFailedToLoad(adError)
                        splashLoadType = SplashType.FAIL
                        if (fetchRemoteConfig == FetchRemoteConfig.DONE) {
                            goToMainScreen()
                        }
                    }

                    override fun onAdSplashReady() {
                        super.onAdSplashReady()
                        if (!isAdded) {
                            return
                        }
                        splashLoadType = SplashType.OPEN_SPLASH
                        onShowSplashScreen()
                    }

                    override fun onInterstitialLoad(interstitialAd: ApInterstitialAd?) {
                        super.onInterstitialLoad(interstitialAd)
                        if (!isAdded) {
                            return
                        }
                        splashLoadType = SplashType.INTERSTITIAL
                        onShowSplashScreen()
                    }

                    override fun onNextAction() {
                        super.onNextAction()
                        if (fetchRemoteConfig == FetchRemoteConfig.DONE) {
                            goToMainScreen()
                        }
                    }
                })
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
            if (splashLoadType == SplashType.OPEN_SPLASH) {
                AperoAd.getInstance().onCheckShowSplashPriorityWhenFail(
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
            } else if (splashLoadType == SplashType.INTERSTITIAL) {
                AperoAd.getInstance().onCheckShowSplashWhenFail(
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
                findNavController().navigate(action)
            } else {
                if (prefUtils.isShowOnBoardingFirstOpen) {
                    val action = SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment()
                    findNavController().navigate(action)
                } else if (prefUtils.profile == null) {
                    val action =
                        SplashFragmentDirections.actionSplashFragmentToProfileEditFragment()
                    action.editMode = false
                    findNavController().navigate(action)
                } else {
                    val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                    findNavController().navigate(action)
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