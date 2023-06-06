package com.blood.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.ads.control.admob.Admob
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.ads.control.ads.wrapper.ApAdError
import com.ads.control.ads.wrapper.ApInterstitialAd
import com.ads.control.ads.wrapper.ApNativeAd
import com.ads.control.funtion.AdCallback
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAdView

/*
* implementation 'apero-inhouse:apero-ads:1.0.6-alpha07'
* */
class AdsUtils {
    companion object {
        val TAG = AdsUtils::class.java.simpleName
    }

    // inter
    val interInsightDetail: InterPriority = InterPriority().apply {
        idAdsPriority = BuildConfig.inter_insight_details_high
        idAdsNormal = BuildConfig.inter_insight_details

        showAfterClick = 2
        countShown = -2
    }
    val interSave: InterPriority = InterPriority().apply {
        idAdsPriority = BuildConfig.inter_save_high
        idAdsNormal = BuildConfig.inter_save

        showAfterClick = 2
    }
    val interInfo: InterPriority = InterPriority().apply {
        idAdsPriority = BuildConfig.inter_info_high
        idAdsNormal = BuildConfig.inter_info
    }

    // native
    val nativeOnBoarding = NativeAds().apply {
        idAdsNativeHigh = BuildConfig.native_onboarding_high
        idAdsNativeNormal = BuildConfig.native_onboarding
    }
    val nativeCreateUser = NativeAds().apply {
        idAdsNativeHigh = BuildConfig.native_create_user_high
        idAdsNativeNormal = BuildConfig.native_create_user
    }
    val nativeBloodPressure = NativeAds().apply {
        idAdsNativeHigh = BuildConfig.native_bloodpressure
    }
    val nativeDefaultValue = NativeAds().apply {
        idAdsNativeHigh = BuildConfig.native_value_high
        idAdsNativeNormal = BuildConfig.native_value
    }
    val nativeLanguage = NativeAds().apply {
        idAdsNativeHigh = BuildConfig.native_language_high
        idAdsNativeNormal = BuildConfig.native_language
    }
    val nativeExit = NativeAds().apply {
        idAdsNativeHigh = BuildConfig.native_exit_high
        idAdsNativeNormal = BuildConfig.native_exit
    }
    val nativeRecentAction = NativeAds().apply {
        idAdsNativeHigh = BuildConfig.native_recent_action
    }

    enum class Status {
        LOADING, NONE, FAIL, SUCCESS, SHOWN
    }

    class NativeAds {
        private val TAG = NativeAds::class.java.name

        private var apNativeAd: ApNativeAd? = null

        var idAdsNativeHigh: String? = null
        var idAdsNativeNormal: String? = null
        var idAdsNativeLow: String? = null

        var isShowAdsHigh: Boolean = true
        var isShowAdsNormal: Boolean = true
        var isShowAdsLow: Boolean = true

        private var status = Status.NONE

        private var layoutCustom: Int = 0
        private var container: FrameLayout? = null


        fun isLoadingAds() = status == Status.LOADING

        private fun isFailOrShownOrNoneAds() =
            status == Status.FAIL || status == Status.SHOWN || status == Status.NONE

        private fun resetShowAfterData() {
            this.layoutCustom = 0
            this.container = null
        }

        fun showAds(
            activity: Activity,
            @LayoutRes layoutCustom: Int,
            container: FrameLayout,
            waitForNewAds: Boolean = false,
            reloadAfterShow: Boolean = false
        ) {
            if (status == Status.LOADING) {
                resetShowAfterData()
                if (canShowAds() && !waitForNewAds) {
                    populateNativeAdView(activity, apNativeAd!!, layoutCustom, container)
                } else {
                    this.layoutCustom = layoutCustom
                    this.container = container
                }
            } else {
                this.layoutCustom = 0
                this.container = null
                if (((!idAdsNativeHigh.isNullOrEmpty() || !idAdsNativeNormal.isNullOrEmpty() || !idAdsNativeLow.isNullOrEmpty()) && !canShowAds()) || !canShowAds()) {
                    loadAds(activity, layoutCustom, true, object : AperoAdCallback() {
                        override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            this@NativeAds.apNativeAd = nativeAd
                            populateNativeAdView(activity, nativeAd, layoutCustom, container)
                            if (reloadAfterShow) {
                                loadAds(activity, layoutCustom, false)
                            }
                            status = Status.SHOWN
                        }

                        override fun onAdFailedToLoad(adError: ApAdError?) {
                            super.onAdFailedToLoad(adError)
                            status = Status.FAIL
                            container.visibility = View.GONE
                        }
                    })
                } else if (canShowAds()) {
                    populateNativeAdView(activity, apNativeAd!!, layoutCustom, container)
                    if (reloadAfterShow) {
                        loadAds(activity, layoutCustom, false)
                    }
                } else {
                    container.visibility = View.GONE
                }
            }
        }

        private fun populateNativeAdView(
            activity: Activity?,
            nativeAd: ApNativeAd,
            @LayoutRes layoutCustom: Int,
            container: FrameLayout
        ) {
            if (activity != null) {
                try {
                    val adView =
                        LayoutInflater.from(activity).inflate(layoutCustom, null) as NativeAdView
                    Admob.getInstance().populateUnifiedNativeAdView(nativeAd.admobNativeAd, adView)
                    container.removeAllViews()
                    container.post {
                        container.requestFocus()
                        container.addView(adView)
                    }
                    Log.d(TAG, "NativeAds populateNativeAdView: Shown")
                    resetShowAfterData()
                } catch (ex: Exception) {
                    Log.d(TAG, "NativeAds populateNativeAdView: Fail")
                    status = Status.FAIL
                    ex.printStackTrace()
                } finally {
                }
            } else {
                Log.d(TAG, "NativeAds populateNativeAdView: Show fail")
            }
        }

        fun loadAds(
            activity: Activity,
            @LayoutRes layoutCustom: Int,
            notReloadIfReady: Boolean = true,
            listener: AperoAdCallback? = null
        ) {
            if (notReloadIfReady && !isFailOrShownOrNoneAds()) return
            Log.d(TAG, "NativeAds loadAds: idAdsNativeHigh $idAdsNativeHigh")
            Log.d(TAG, "NativeAds loadAds: idAdsNativeNormal $idAdsNativeNormal")
            Log.d(TAG, "NativeAds loadAds: idAdsNativeLow $idAdsNativeLow")
            status = Status.LOADING
            AperoAd.getInstance().loadNative3SameTime(activity,
                idAdsNativeHigh,
                idAdsNativeNormal,
                idAdsNativeLow,
                layoutCustom,
                object : AperoAdCallback() {
                    override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        this@NativeAds.apNativeAd = nativeAd
                        status = Status.SUCCESS

                        Log.d(TAG, "NativeAds onNativeAdLoaded")
                        listener?.onNativeAdLoaded(nativeAd)

                        if (this@NativeAds.container != null) {
                            populateNativeAdView(
                                activity, nativeAd, this@NativeAds.layoutCustom, container!!
                            )
                        }
                    }

                    override fun onAdFailedToLoad(adError: ApAdError?) {
                        super.onAdFailedToLoad(adError)
                        status = Status.FAIL
                        Log.d(TAG, "NativeAds onAdFailedToLoad")
                        listener?.onAdFailedToLoad(adError)
                    }
                })
        }

        private fun canShowAds(): Boolean {
            if (apNativeAd != null && (isShowAdsHigh || isShowAdsNormal || isShowAdsLow)) {
                return true
            }
            return false
        }
    }

    class InterPriority {
        private val TAG = InterPriority::class.java.name

        private var interAdsPriority: ApInterstitialAd? = null
        private var interAdsNormal: ApInterstitialAd? = null
        var idAdsPriority: String? = null
        var idAdsNormal: String? = null

        var showAfterClick: Int = 0
        var countShown: Int = 0

        companion object {
            var lastShowInter: Long = 0L
            private var intervalTime: Long = 0L
            private var isShowByIntervalTime: Boolean = false

            fun setShowByIntervalTime(isShow: Boolean, time: Long = 0) {
                intervalTime = time
                isShowByIntervalTime = isShow
            }
        }

        private var statusHigh = Status.NONE
        private var statusNormal = Status.NONE

        private fun mustReloadAds(): Boolean =
            (statusHigh == Status.SHOWN || statusNormal == Status.SHOWN) || (statusHigh == Status.NONE && statusNormal == Status.NONE) || (statusHigh == Status.FAIL && statusNormal == Status.FAIL)

        var isShowHighAds: Boolean = true
        var isShowNormalAds: Boolean = true

        private fun interAdsPriorityLoaded(): Boolean =
            interAdsPriority?.interstitialAd != null && interAdsPriority!!.isReady

        private fun interAdsNormalLoaded(): Boolean =
            interAdsNormal?.interstitialAd != null && interAdsNormal!!.isReady

        fun checkAdsRepairedAll(): Boolean {
            if (interAdsPriorityLoaded() && interAdsNormalLoaded()) {
                return true
            }
            return false
        }

        private fun canShowAds(): Boolean {
            if ((interAdsPriorityLoaded() && isShowHighAds) || (interAdsNormalLoaded() && isShowNormalAds)) {
                return true
            }
            return false
        }

        private fun checkShowByIntervalTime(): Boolean {
            return if (isShowByIntervalTime) {
                System.currentTimeMillis() - lastShowInter > intervalTime
            } else {
                true
            }
        }

        private fun checkShowBySkip(): Boolean {
            if (showAfterClick == 0) {
                return true
            }
            if (countShown < 0) {
                countShown++
                return false
            }
            if (countShown == 0 || countShown % (showAfterClick + 1) == 0) {
                countShown++
                return true
            }
            countShown++
            return false
        }

        fun showInterAdsBeforeNavigate(
            context: Context, reloadAfterShow: Boolean = false, nextActionCallBack: (() -> Unit)
        ) {
            val finishCallback: (Boolean) -> Unit = run {
                { isReload ->
                    nextActionCallBack()
                    if (isReload) {
                        loadInterPrioritySameTime(context, false, null)
                    }
                }
            }
            if (canShowAds() && (!isShowByIntervalTime || checkShowByIntervalTime()) && checkShowBySkip()) {
                if (interAdsPriorityLoaded() && !interAdsNormalLoaded()) {
                    AperoAd.getInstance().forceShowInterstitial(
                        context, interAdsPriority, object : AperoAdCallback() {
                            override fun onNextAction() {
                                super.onNextAction()
                                finishCallback(reloadAfterShow)
                                lastShowInter = System.currentTimeMillis()
                                statusHigh = Status.SHOWN
                                Log.d(
                                    TAG, "inter forceShowInterstitial: idAdsPriority $idAdsPriority"
                                )
                            }

                            override fun onAdFailedToShow(adError: ApAdError?) {
                                super.onAdFailedToShow(adError)
                                Log.d(TAG, "inter onAdFailedToShow: idAdsPriority $idAdsPriority")
                                countShown = 0
                            }
                        }, false
                    )
                } else if (!interAdsPriorityLoaded() && interAdsNormalLoaded()) {
                    AperoAd.getInstance().forceShowInterstitial(
                        context, interAdsNormal, object : AperoAdCallback() {
                            override fun onNextAction() {
                                super.onNextAction()
                                finishCallback(reloadAfterShow)
                                lastShowInter = System.currentTimeMillis()
                                statusNormal = Status.SHOWN
                                Log.d(TAG, "forceShowInterstitial: idAdsNormal $idAdsNormal")
                            }

                            override fun onAdFailedToShow(adError: ApAdError?) {
                                super.onAdFailedToShow(adError)
                                countShown = 0
                                Log.d(TAG, "inter onAdFailedToShow: idAdsNormal $idAdsNormal")
                            }
                        }, false
                    )
                } else {
                    AperoAd.getInstance().forceShowInterstitial(
                        context, interAdsPriority, object : AperoAdCallback() {
                            override fun onNextAction() {
                                super.onNextAction()
                                finishCallback(reloadAfterShow)
                                lastShowInter = System.currentTimeMillis()
                                statusHigh = Status.SHOWN
                                Log.d(TAG, "forceShowInterstitial: idAdsPriority $idAdsPriority")
                            }


                            override fun onAdFailedToShow(adError: ApAdError?) {
                                super.onAdFailedToShow(adError)
                                Log.d(TAG, "inter onAdFailedToShow: idAdsPriority $idAdsPriority")

                                //show Normal Ads If fail Priority
                                AperoAd.getInstance().forceShowInterstitial(
                                    context, interAdsNormal, object : AperoAdCallback() {
                                        override fun onNextAction() {
                                            super.onNextAction()
                                            finishCallback(reloadAfterShow)
                                            lastShowInter = System.currentTimeMillis()
                                            statusNormal = Status.SHOWN
                                            Log.d(
                                                TAG,
                                                "forceShowInterstitial: idAdsNormal $idAdsNormal"
                                            )
                                        }

                                        override fun onAdFailedToShow(adError: ApAdError?) {
                                            super.onAdFailedToShow(adError)
                                            countShown = 0
                                            Log.d(
                                                TAG,
                                                "inter onAdFailedToShow: idAdsNormal $idAdsNormal"
                                            )
                                        }
                                    }, false
                                )
                            }
                        }, false
                    )
                }
            } else {
                finishCallback(reloadAfterShow && mustReloadAds())
            }
        }

        fun loadInterPrioritySameTime(
            context: Context, notLoadIfReady: Boolean = true, listener: Listener? = null
        ) {
            if (notLoadIfReady && !mustReloadAds() || (statusHigh == Status.LOADING || statusNormal == Status.LOADING)) return

            statusHigh = Status.NONE
            if (isShowHighAds && !idAdsPriority.isNullOrEmpty()) {
                statusHigh = Status.LOADING
                Log.d(TAG, "onInterstitialLoad: idAdsPriority $idAdsPriority")
                AperoAd.getInstance()
                    .getInterstitialAds(context, idAdsPriority, object : AperoAdCallback() {
                        override fun onInterstitialLoad(interstitialAd: ApInterstitialAd?) {
                            super.onInterstitialLoad(interstitialAd)
                            Log.d(TAG, "onInterstitialLoad: idAdsPriority SUCCESS $idAdsPriority")
                            interAdsPriority = interstitialAd
                            listener?.onAdsPriorityLoaded(interstitialAd)
                            statusHigh = Status.SUCCESS
                        }

                        override fun onAdFailedToLoad(adError: ApAdError?) {
                            super.onAdFailedToLoad(adError)
                            Log.d(TAG, "onAdFailedToLoad: idAdsPriority FAIL $idAdsPriority")
                            interAdsPriority = null
                            listener?.onAdsPriorityLoadFail(adError)
                            statusHigh = Status.FAIL
                        }
                    })
            }

            statusNormal = Status.NONE
            if (isShowNormalAds && !idAdsNormal.isNullOrEmpty()) {
                statusNormal = Status.LOADING
                Log.d(TAG, "onInterstitialLoad: idAdsNormal $idAdsNormal")
                AperoAd.getInstance()
                    .getInterstitialAds(context, idAdsNormal, object : AperoAdCallback() {
                        override fun onInterstitialLoad(interstitialAd: ApInterstitialAd?) {
                            super.onInterstitialLoad(interstitialAd)
                            Log.d(TAG, "onInterstitialLoad: idAdsNormal SUCCESS $idAdsNormal")
                            interAdsNormal = interstitialAd
                            listener?.onAdsNormalLoaded(interstitialAd)
                            statusNormal = Status.SUCCESS
                        }

                        override fun onAdFailedToLoad(adError: ApAdError?) {
                            super.onAdFailedToLoad(adError)
                            Log.d(TAG, "onAdFailedToLoad: idAdsNormal FAIL $idAdsNormal")
                            interAdsNormal = null
                            listener?.onAdsNormalLoadFail(adError)
                            statusNormal = Status.FAIL
                        }
                    })
            }
        }

        interface Listener {
            fun onAdsPriorityLoaded(interstitialAd: ApInterstitialAd?)

            fun onAdsPriorityLoadFail(adError: ApAdError?)

            fun onAdsNormalLoaded(interstitialAd: ApInterstitialAd?)

            fun onAdsNormalLoadFail(adError: ApAdError?)
        }
    }

    object BannerUtils {

        fun FrameLayout.loadBanner(
            activity: Activity, idAdsBanner: String, condition: Boolean = true
        ) {
            if (condition) {
                this.visibility = View.VISIBLE
                try {
                    AperoAd.getInstance()
                        .loadBannerFragment(activity, idAdsBanner, this, object : AdCallback() {
                            override fun onAdFailedToLoad(i: LoadAdError?) {
                                super.onAdFailedToLoad(i)
                                this@loadBanner.visibility = View.GONE
                            }

                            override fun onAdFailedToShow(adError: AdError?) {
                                super.onAdFailedToShow(adError)
                                this@loadBanner.visibility = View.GONE
                            }
                        })
                } catch (ex: java.lang.Exception) {
                    this@loadBanner.visibility = View.GONE
                }
            } else {
                this.visibility = View.GONE
            }
        }
    }
}