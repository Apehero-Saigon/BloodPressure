package com.blood.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.ads.control.admob.Admob
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.ads.control.ads.wrapper.ApAdError
import com.ads.control.ads.wrapper.ApInterstitialAd
import com.ads.control.ads.wrapper.ApNativeAd
import com.ads.control.funtion.AdCallback
import com.blood.utils.ViewUtils.gone
import com.blood.utils.ViewUtils.invisible
import com.blood.utils.ViewUtils.visible
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAdView
import java.util.concurrent.locks.Condition

class AdsUtils {

    // inter
    val interBloodDetails: InterPriority = InterPriority()
    val interMeasure: InterPriority = InterPriority()
    val interInfo: InterPriority = InterPriority().apply {
        showAfterClick = 1
    }
    val interSaveProfile: InterPriority = InterPriority()

    // native
    val nativeOnBoarding1 = NativeAds()
    val nativeOnBoarding2 = NativeAds()
    val nativeOnBoarding3 = NativeAds()
    val nativeAddBloodPressure = NativeAds()
    val nativeLanguage = NativeAds()
    val nativeRecentAction = NativeAds()

    enum class Status {
        LOADING, NONE, FAIL, SUCCESS, SHOWN
    }

    class NativeAds {
        var apNativeAdHigh: ApNativeAd? = null
        var apNativeAdNormal: ApNativeAd? = null

        var idAdsNativeHigh: String? = null
        var idAdsNativeNormal: String? = null

        var isShowHighAds: Boolean = true
        var isShowNormalAds: Boolean = true

        private var statusHigh = Status.NONE
        private var statusNormal = Status.NONE

        private var layoutCustom: Int = 0
        private var container: FrameLayout? = null
        private var keepLayout: Boolean = false


        fun isLoadingAds() = statusHigh == Status.LOADING || statusNormal == Status.LOADING

        fun isFailOrShownOrNoneAds() =
            (statusHigh == Status.FAIL && statusNormal == Status.FAIL) || (statusHigh == Status.SHOWN || statusNormal == Status.SHOWN) || (statusHigh == Status.NONE || statusNormal == Status.NONE)

        private fun resetShowAfterData() {
            this.layoutCustom = 0
            this.container = null
            this.keepLayout = false
        }

        fun showAds(
            activity: Activity,
            idAdsNativeHigh: String?,
            idAdsNativeNormal: String?,
            @LayoutRes layoutCustom: Int,
            container: FrameLayout,
            keepLayout: Boolean = false,
            reloadAfterShow: Boolean = false
        ) {
            if (statusHigh == Status.LOADING && statusNormal == Status.LOADING) {
                resetShowAfterData()
                if (apNativeAdHigh != null || apNativeAdNormal != null) {
                    populateNativeAdView(
                        activity,
                        if (apNativeAdHigh != null) apNativeAdHigh!! else apNativeAdNormal!!,
                        layoutCustom,
                        container
                    )
                }
            } else {
                this.layoutCustom = 0
                this.container = null
                this.keepLayout = false
                if (((!idAdsNativeHigh.isNullOrEmpty() || !idAdsNativeNormal.isNullOrEmpty()) && !canShowAds()) || !canShowAds()) {
                    loadAds(activity,
                        idAdsNativeHigh,
                        idAdsNativeNormal,
                        layoutCustom,
                        object : Listener {
                            override fun onAdsPriorityLoaded(apNativeAd: ApNativeAd) {
                                Log.d(
                                    NativeAds::class.java.simpleName,
                                    "show: idAdsNativeHigh SUCCESS $idAdsNativeHigh"
                                )
                                populateNativeAdView(
                                    activity, apNativeAd, layoutCustom, container
                                )
                                if (reloadAfterShow) {
                                    loadAds(
                                        activity, idAdsNativeHigh, idAdsNativeNormal, layoutCustom
                                    )
                                }
                                statusHigh = Status.SHOWN
                            }


                            override fun onAdsNormalLoaded(apNativeAd: ApNativeAd) {
                                Log.d(
                                    NativeAds::class.java.simpleName,
                                    "show: idAdsNativeNormal SUCCESS $idAdsNativeNormal"
                                )
                                populateNativeAdView(
                                    activity, apNativeAd, layoutCustom, container
                                )
                                if (reloadAfterShow) {
                                    loadAds(
                                        activity, idAdsNativeHigh, idAdsNativeNormal, layoutCustom
                                    )
                                }
                                statusNormal = Status.SHOWN
                            }

                            override fun onAdsLoadFail(adError: ApAdError?) {
                                statusHigh = Status.FAIL
                                statusNormal = Status.FAIL
                                if (keepLayout) {
                                    container.invisible()
                                } else {
                                    container.gone()
                                }
                            }

                        })
                } else if (canShowAds()) {
                    if (apNativeAdHigh != null) {
                        Log.d(
                            NativeAds::class.java.simpleName,
                            "show: idAdsNativeHigh SUCCESS $idAdsNativeHigh"
                        )
                    } else {
                        Log.d(
                            NativeAds::class.java.simpleName,
                            "show: idAdsNativeNormal SUCCESS $idAdsNativeNormal"
                        )
                    }
                    populateNativeAdView(
                        activity,
                        if (apNativeAdHigh != null) apNativeAdHigh!! else apNativeAdNormal!!,
                        layoutCustom,
                        container
                    )
                    if (reloadAfterShow) {
                        loadAds(activity, idAdsNativeHigh, idAdsNativeNormal, layoutCustom)
                    }
                } else {
                    if (keepLayout) {
                        container.invisible()
                    } else {
                        container.gone()
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
                resetShowAfterData()
            } else {
                println("### populateNativeAdView: in null activity")
            }
        }

        fun loadAds(
            activity: Activity,
            idAdsNativeHigh: String?,
            idAdsNativeNormal: String?,
            @LayoutRes layoutCustom: Int,
            listener: Listener? = null
        ) {
            println("NativeAds loadAds: idAdsNativeHigh $idAdsNativeHigh")
            println("NativeAds loadAds: idAdsNativeNormal $idAdsNativeNormal")
            statusHigh = Status.LOADING
            statusNormal = Status.LOADING
            this.idAdsNativeHigh = idAdsNativeHigh
            this.idAdsNativeNormal = idAdsNativeNormal
            if (isShowHighAds && !idAdsNativeHigh.isNullOrEmpty()) {
                AperoAd.getInstance().loadNativeAdResultCallback(activity,
                    idAdsNativeHigh,
                    layoutCustom,
                    object : AperoAdCallback() {
                        override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            apNativeAdHigh = nativeAd
                            statusHigh = Status.SUCCESS
                            println("NativeAds loadAds: idAdsNativeHigh SUCCESS $idAdsNativeHigh")
                            listener?.onAdsPriorityLoaded(nativeAd)
                        }

                        override fun onAdFailedToLoad(adError: ApAdError?) {
                            super.onAdFailedToLoad(adError)
                            apNativeAdHigh = null
                            statusHigh = Status.FAIL
                            println("NativeAds loadAds: FAIL idAdsNativeHigh $idAdsNativeHigh")

                            if (statusNormal == Status.SUCCESS && apNativeAdNormal != null) {
                                listener?.onAdsPriorityLoaded(apNativeAdNormal!!)
                            } else if (statusNormal == Status.FAIL) {
                                listener?.onAdsLoadFail(adError)
                            }
                        }
                    })
            }
            if (isShowNormalAds && !idAdsNativeNormal.isNullOrEmpty()) {
                AperoAd.getInstance().loadNativeAdResultCallback(activity,
                    idAdsNativeNormal,
                    layoutCustom,
                    object : AperoAdCallback() {
                        override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            apNativeAdNormal = nativeAd
                            println("NativeAds loadAds: idAdsNativeNormal SUCCESS $idAdsNativeNormal")
                            statusNormal = Status.SUCCESS

                            if (statusHigh == Status.FAIL) {
                                listener?.onAdsPriorityLoaded(nativeAd)
                            }
                        }

                        override fun onAdFailedToLoad(adError: ApAdError?) {
                            super.onAdFailedToLoad(adError)
                            apNativeAdNormal = null
                            statusNormal = Status.FAIL
                            println("NativeAds loadAds: FAIL idAdsNativeNormal $idAdsNativeNormal")

                            if (statusNormal == Status.FAIL) {
                                listener?.onAdsLoadFail(adError)
                            }
                        }
                    })
            }
        }

        private fun canShowAds(): Boolean {
            if (apNativeAdHigh != null || apNativeAdNormal != null) {
                return true
            }
            return false
        }

        interface Listener {
            fun onAdsPriorityLoaded(apNativeAd: ApNativeAd)

            fun onAdsNormalLoaded(apNativeAd: ApNativeAd)

            fun onAdsLoadFail(adError: ApAdError?)
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

        private fun resetDataAds() {
            interAdsPriority = null
            interAdsNormal = null
        }

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
            if (showAfterClick == 0 || (showAfterClick > 0 && countShown < showAfterClick)) {
                countShown++
                return true
            }
            countShown = 0
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
            return
            if (notLoadIfReady && !mustReloadAds()) return
            if (isReloadFailAds) {
                resetDataAds()
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
            if (condition && NetworkUtils.isInternetAvailable()) {
                this.visible()
                AperoAd.getInstance()
                    .loadBannerFragment(activity, idAdsBanner, this, object : AdCallback() {
                        override fun onAdFailedToLoad(i: LoadAdError?) {
                            super.onAdFailedToLoad(i)
                            this@loadBanner.invisible()
                        }

                        override fun onAdFailedToShow(adError: AdError?) {
                            super.onAdFailedToShow(adError)
                            this@loadBanner.invisible()
                        }
                    })
            } else {
                this.gone()
            }
        }
    }
}