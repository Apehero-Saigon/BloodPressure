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
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAdView

/*
* implementation 'apero-inhouse:apero-ads:1.0.6-alpha07'
* */
class AdsUtils {

    // inter
    val interInsightDetail: InterPriority = InterPriority().apply {
        showAfterClick = 2
        countShown = -2
    }
    val interSave: InterPriority = InterPriority().apply {
        showAfterClick = 2
    }
    val interInfo: InterPriority = InterPriority()

    // native
    val nativeOnBoarding1 = NativeAds()
    val nativeOnBoarding2 = NativeAds()
    val nativeOnBoarding3 = NativeAds()
    val nativeCreateUser = NativeAds()
    val nativeBloodPressure = NativeAds()
    val nativeDefaultValue = NativeAds()
    val nativeLanguage = NativeAds()
    val nativeExit = NativeAds()
    val nativeRecentAction = NativeAds()

    enum class Status {
        LOADING, NONE, FAIL, SUCCESS, SHOWN
    }

    class NativeAds {
        var apNativeAd: ApNativeAd? = null

        var idAdsNativeHigh: String? = null
        var idAdsNativeNormal: String? = null
        var idAdsNativeLow: String? = null

        var isShowAdsHigh: Boolean = true
        var isShowAdsNormal: Boolean = true
        var isShowAdsLow: Boolean = true

        private var status = Status.NONE

        var clearAdsAfterShow = false

        private var layoutCustom: Int = 0
        private var container: FrameLayout? = null
        private var keepLayout: Boolean = false


        fun isLoadingAds() = status == Status.LOADING

        fun isFailOrShownOrNoneAds() =
            status == Status.FAIL || status == Status.SHOWN || status == Status.NONE

        private fun resetShowAfterData() {
            this.layoutCustom = 0
            this.container = null
            this.keepLayout = false
        }

        fun showAds(
            activity: Activity,
            idAdsNativeHigh: String?,
            idAdsNativeNormal: String?,
            idAdsNativeLow: String?,
            @LayoutRes layoutCustom: Int,
            container: FrameLayout,
            keepLayout: Boolean = false,
            reloadAfterShow: Boolean = false
        ) {
            if (status == Status.LOADING) {
                resetShowAfterData()
                if (canShowAds()) {
                    populateNativeAdView(activity, apNativeAd!!, layoutCustom, container)
                } else {
                    this.layoutCustom = layoutCustom
                    this.container = container
                }
            } else {
                this.layoutCustom = 0
                this.container = null
                this.keepLayout = false
                if (((!idAdsNativeHigh.isNullOrEmpty() || !idAdsNativeNormal.isNullOrEmpty() || !idAdsNativeLow.isNullOrEmpty()) && !canShowAds()) || !canShowAds()) {
                    loadAds(activity,
                        idAdsNativeHigh,
                        idAdsNativeNormal,
                        idAdsNativeLow,
                        layoutCustom,
                        true,
                        object : AperoAdCallback() {
                            override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                                super.onNativeAdLoaded(nativeAd)
                                this@NativeAds.apNativeAd = nativeAd
                                populateNativeAdView(
                                    activity, nativeAd, layoutCustom, container
                                )
                                if (reloadAfterShow) {
                                    loadAds(
                                        activity,
                                        idAdsNativeHigh,
                                        idAdsNativeNormal,
                                        idAdsNativeLow,
                                        layoutCustom
                                    )
                                }
                                status = Status.SHOWN
                            }

                            override fun onAdFailedToLoad(adError: ApAdError?) {
                                super.onAdFailedToLoad(adError)
                                status = Status.FAIL
                                if (keepLayout) {
                                    container.visibility = View.INVISIBLE
                                } else {
                                    container.visibility = View.GONE
                                }
                            }
                        })
                } else if (canShowAds()) {
                    populateNativeAdView(
                        activity, apNativeAd!!, layoutCustom, container
                    )
                    if (reloadAfterShow) {
                        loadAds(
                            activity,
                            idAdsNativeHigh,
                            idAdsNativeNormal,
                            idAdsNativeLow,
                            layoutCustom
                        )
                    }
                } else {
                    if (keepLayout) {
                        container.visibility = View.INVISIBLE
                    } else {
                        container.visibility = View.GONE
                    }
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
                val adView =
                    LayoutInflater.from(activity).inflate(layoutCustom, null) as NativeAdView
                Admob.getInstance().populateUnifiedNativeAdView(nativeAd.admobNativeAd, adView)
                container.removeAllViews()
                container.post {
                    container.addView(adView)
                }
                Log.d(AdsUtils::class.java.simpleName, "NativeAds populateNativeAdView: Shown")
                resetShowAfterData()
            } else {
                Log.d(AdsUtils::class.java.simpleName, "NativeAds populateNativeAdView: Show fail")
            }
            if (clearAdsAfterShow) {
                this@NativeAds.apNativeAd?.admobNativeAd?.destroy()
                this@NativeAds.apNativeAd = null
            }
        }

        fun loadAds(
            activity: Activity,
            idAdsNativeHigh: String?,
            idAdsNativeNormal: String?,
            idAdsNativeLow: String?,
            @LayoutRes layoutCustom: Int,
            notReloadIfReady: Boolean = true,
            listener: AperoAdCallback? = null
        ) {
            if (notReloadIfReady && !isFailOrShownOrNoneAds()) return
            Log.d(
                AdsUtils::class.java.simpleName,
                "NativeAds loadAds: idAdsNativeHigh $idAdsNativeHigh"
            )
            Log.d(
                AdsUtils::class.java.simpleName,
                "NativeAds loadAds: idAdsNativeNormal $idAdsNativeNormal"
            )
            Log.d(
                AdsUtils::class.java.simpleName, "NativeAds loadAds: idAdsNativeLow $idAdsNativeLow"
            )
            status = Status.LOADING
            this.idAdsNativeHigh = idAdsNativeHigh
            this.idAdsNativeNormal = idAdsNativeNormal
            this.idAdsNativeLow = idAdsNativeLow

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

                        Log.d(AdsUtils::class.java.simpleName, "NativeAds onNativeAdLoaded")
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
                        this@NativeAds.apNativeAd = null

                        Log.d(AdsUtils::class.java.simpleName, "NativeAds onAdFailedToLoad")
                        listener?.onAdFailedToLoad(adError)
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
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
                        loadInterPrioritySameTime(
                            context, idAdsPriority, idAdsNormal, reloadAfterShow, false
                        )
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
                            }

                            override fun onAdFailedToShow(adError: ApAdError?) {
                                super.onAdFailedToShow(adError)

                                //show Normal Ads If fail Priority
                                AperoAd.getInstance().forceShowInterstitial(
                                    context, interAdsNormal, object : AperoAdCallback() {
                                        override fun onNextAction() {
                                            super.onNextAction()
                                            finishCallback(reloadAfterShow)
                                            lastShowInter = System.currentTimeMillis()
                                            statusNormal = Status.SHOWN
                                        }
                                    }, false
                                )
                            }
                        }, false
                    )
                }
            } else {
                finishCallback(mustReloadAds())
            }
        }

        fun loadInterPrioritySameTime(
            context: Context,
            idAdsPriority: String?,
            idAdsNormal: String?,
            isReloadFailAds: Boolean = true,
            notLoadIfReady: Boolean = true,
            listener: Listener? = null
        ) {
            if (notLoadIfReady && !mustReloadAds()) return
            if (isReloadFailAds) {
                if (statusHigh == Status.FAIL) {
                    interAdsPriority = null
                }
                if (statusNormal == Status.FAIL) {
                    interAdsNormal = null
                }
            }
            this.idAdsPriority = idAdsPriority
            this.idAdsNormal = idAdsNormal
            if (isShowHighAds && !idAdsPriority.isNullOrEmpty()) {
                statusHigh = Status.LOADING
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

            if (isShowNormalAds && !idAdsNormal.isNullOrEmpty()) {
                statusNormal = Status.LOADING
                AperoAd.getInstance()
                    .getInterstitialAds(context, idAdsNormal, object : AperoAdCallback() {
                        override fun onInterstitialLoad(interstitialAd: ApInterstitialAd?) {
                            super.onInterstitialLoad(interstitialAd)
                            Log.d(TAG, "onInterstitialLoad: idAdsNormal SUCCESS $idAdsNormal")
                            interAdsNormal = interstitialAd
                            listener?.onAdsNormalLoaded(interstitialAd)
                            statusHigh = Status.SUCCESS
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
                AperoAd.getInstance()
                    .loadBannerFragment(activity, idAdsBanner, this, object : AdCallback() {
                        override fun onAdFailedToLoad(i: LoadAdError?) {
                            super.onAdFailedToLoad(i)
                            this@loadBanner.visibility = View.INVISIBLE
                        }

                        override fun onAdFailedToShow(adError: AdError?) {
                            super.onAdFailedToShow(adError)
                            this@loadBanner.visibility = View.INVISIBLE
                        }
                    })
            } else {
                this.visibility = View.GONE
            }
        }
    }
}