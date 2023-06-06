package com.blood.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.ads.control.admob.Admob
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.ads.control.ads.wrapper.ApAdError
import com.ads.control.ads.wrapper.ApInterstitialAd
import com.ads.control.ads.wrapper.ApNativeAd
import com.ads.control.funtion.AdCallback
import com.blood.App
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
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

    val prefUtils = PrefUtils.instant

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
    val nativeOnBoarding = NativeAds(
        R.layout.native_medium,
        nativeLoaderHigh = NativeAds.NativeLoader(
            BuildConfig.native_onboarding_high, 3, prefUtils.isShowNativeOnBoarding
        ),
        nativeLoaderNormal = NativeAds.NativeLoader(
            BuildConfig.native_onboarding, 1, prefUtils.isShowNativeOnBoarding
        )
    )
    val nativeCreateUser = NativeAds(
        R.layout.layout_native_medium_custom,
        nativeLoaderHigh = NativeAds.NativeLoader(
            BuildConfig.native_create_user_high, 3, prefUtils.isShowNativeCreateUser
        ),
        nativeLoaderNormal = NativeAds.NativeLoader(
            BuildConfig.native_create_user, 1, prefUtils.isShowNativeCreateUser
        )
    )
    val nativeBloodPressure = NativeAds(
        R.layout.layout_native_medium_custom,
        nativeLoaderHigh = NativeAds.NativeLoader(
            BuildConfig.native_bloodpressure, 1, prefUtils.isShowNativeBloodPressure
        )
    )
    val nativeDefaultValue = NativeAds(
        R.layout.native_medium,
        nativeLoaderHigh = NativeAds.NativeLoader(
            BuildConfig.native_value_high, 3, prefUtils.isShowNativeDefaultValue
        ),
        nativeLoaderNormal = NativeAds.NativeLoader(
            BuildConfig.native_value, 1, prefUtils.isShowNativeDefaultValue
        )
    )
    val nativeLanguage = NativeAds(
        R.layout.native_medium,
        nativeLoaderHigh = NativeAds.NativeLoader(
            BuildConfig.native_language_high, 3, prefUtils.isShowNativeLanguage
        ),
        nativeLoaderNormal = NativeAds.NativeLoader(
            BuildConfig.native_language, 1, prefUtils.isShowNativeLanguage
        )
    )
    val nativeExit = NativeAds(
        R.layout.native_medium,
        nativeLoaderHigh = NativeAds.NativeLoader(
            BuildConfig.native_exit_high, 3, prefUtils.isShowNativeExitHigh
        ),
        nativeLoaderNormal = NativeAds.NativeLoader(
            BuildConfig.native_exit, 1, prefUtils.isShowNativeExit
        )
    )
    val nativeRecentAction = NativeAds(
        R.layout.native_medium,
        nativeLoaderHigh = NativeAds.NativeLoader(
            BuildConfig.native_recent_action, 1, prefUtils.isShowNativeRecentAction
        )
    )

    enum class Status {
        LOADING, NONE, FAIL, SUCCESS, SHOWN
    }

    class NativeAds(
        layoutCustom: Int,
        val nativeLoaderHigh: NativeLoader? = null,
        val nativeLoaderNormal: NativeLoader? = null,
        val nativeLoaderLow: NativeLoader? = null,
    ) {
        init {
            nativeLoaderHigh?.layoutCustom = layoutCustom
            nativeLoaderNormal?.layoutCustom = layoutCustom
            nativeLoaderLow?.layoutCustom = layoutCustom
        }

        private val TAG = NativeAds::class.java.name

        private var container: FrameLayout? = null
        private var isReadLoadAfterShow: Boolean = true
        private fun hasAvailableAds() =
            nativeLoaderHigh?.canShowAds() == true || nativeLoaderNormal?.canShowAds() == true || nativeLoaderLow?.canShowAds() == true

        fun showAds(
            activity: Activity,
            container: FrameLayout,
            waitForNewAds: Boolean = true,
            reloadAfterShow: Boolean = true
        ) {
            if (nativeLoaderHigh?.isLoading() == true || nativeLoaderNormal?.isLoading() == true || nativeLoaderLow?.isLoading() == true) {
                if (waitForNewAds) {
                    this.container = container
                } else {
                    if (hasAvailableAds()) {
                        if (nativeLoaderHigh?.canShowAds() == true) {
                            Log.d(TAG, "showAds: high id(${nativeLoaderHigh.idAds})")
                            nativeLoaderHigh.showAds(activity, container, reloadAfterShow)
                        } else if (nativeLoaderNormal?.canShowAds() == true) {
                            Log.d(TAG, "showAds: normal id(${nativeLoaderNormal.idAds})")
                            nativeLoaderNormal.showAds(activity, container, reloadAfterShow)
                        } else if (nativeLoaderLow?.canShowAds() == true) {
                            Log.d(TAG, "showAds: lowe id(${nativeLoaderLow.idAds})")
                            nativeLoaderLow.showAds(activity, container, reloadAfterShow)
                        } else {
                            container.visibility = View.GONE
                        }
                    } else {
                        container.visibility = View.GONE
                    }
                }
            } else {
                this.container = null
                this.isReadLoadAfterShow = reloadAfterShow
                if (hasAvailableAds()) {
                    if (nativeLoaderHigh?.canShowAds() == true) {
                        Log.d(TAG, "showAds: high id(${nativeLoaderHigh.idAds})")
                        nativeLoaderHigh.showAds(activity, container, reloadAfterShow)
                    } else if (nativeLoaderNormal?.canShowAds() == true) {
                        Log.d(TAG, "showAds: normal id(${nativeLoaderNormal.idAds})")
                        nativeLoaderNormal.showAds(activity, container, reloadAfterShow)
                    } else if (nativeLoaderLow?.canShowAds() == true) {
                        Log.d(TAG, "showAds: low id(${nativeLoaderLow.idAds})")
                        nativeLoaderLow.showAds(activity, container, reloadAfterShow)
                    } else {
                        container.visibility = View.GONE
                    }
                } else {
                    loadAds(activity, object : NativeLoaderListener() {
                        override fun onNativeFloorLoaded(nativeLoader: NativeLoader) {
                            super.onNativeFloorLoaded(nativeLoader)
                            Log.d(TAG, "showAds: id(${nativeLoader.idAds})")
                            nativeLoader.showAds(activity, container)
                        }

                        override fun onAdFailedToLoad(adError: ApAdError?) {
                            super.onAdFailedToLoad(adError)
                            Log.d(TAG, "showAds: Fail id(${adError?.message})")
                            container.visibility = View.GONE
                        }
                    })
                }
            }
        }

        fun loadAds(activity: Activity, listener: NativeLoaderListener? = null) {
            nativeLoaderHigh?.loadAds(activity, object : AperoAdCallback() {
                override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                    super.onNativeAdLoaded(nativeAd)
                    listener?.onNativeFloorLoaded(nativeLoaderHigh)
                    if (container != null) {
                        nativeLoaderHigh.showAds(activity, container!!, isReadLoadAfterShow)
                    }
                }

                override fun onAdFailedToLoad(adError: ApAdError?) {
                    super.onAdFailedToLoad(adError)
                    if (nativeLoaderNormal?.isLoadSuccess() == false && nativeLoaderLow?.isLoadSuccess() == false) {
                        listener?.onAdFailedToLoad(adError)
                    }
                }
            })
            nativeLoaderNormal?.loadAds(activity, object : AperoAdCallback() {
                override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                    super.onNativeAdLoaded(nativeAd)
                    if (nativeLoaderHigh?.isLoadSuccess() == false) {
                        listener?.onNativeFloorLoaded(nativeLoaderNormal)
                        if (container != null) {
                            nativeLoaderHigh.showAds(activity, container!!, isReadLoadAfterShow)
                        }
                    }
                }

                override fun onAdFailedToLoad(adError: ApAdError?) {
                    super.onAdFailedToLoad(adError)
                    if (nativeLoaderHigh?.isLoadSuccess() == false && nativeLoaderLow?.isLoadSuccess() == false) {
                        listener?.onAdFailedToLoad(adError)
                    }
                }
            })
            nativeLoaderLow?.loadAds(activity, object : AperoAdCallback() {
                override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                    super.onNativeAdLoaded(nativeAd)
                    if (nativeLoaderHigh?.isLoadSuccess() == false && nativeLoaderNormal?.isLoadSuccess() == false) {
                        listener?.onNativeFloorLoaded(nativeLoaderLow)
                        if (container != null) {
                            nativeLoaderHigh.showAds(activity, container!!, isReadLoadAfterShow)
                        }
                    }
                }

                override fun onAdFailedToLoad(adError: ApAdError?) {
                    super.onAdFailedToLoad(adError)
                    if (nativeLoaderHigh?.isLoadSuccess() == false && nativeLoaderNormal?.isLoadSuccess() == false) {
                        listener?.onAdFailedToLoad(adError)
                    }
                }
            })
        }

        class NativeLoader(
            val idAds: String, private val reloadTime: Int, private val condition: Boolean = true
        ) {
            var layoutCustom: Int = 0
            private var nativeAd: ApNativeAd? = null
            private var status: Status = Status.NONE

            private fun isFailOrShownOrNoneAds() =
                status == Status.FAIL || status == Status.SHOWN || status == Status.NONE

            fun isLoadSuccess() = status == Status.SUCCESS

            fun isLoading() = status == Status.LOADING

            fun canShowAds(): Boolean {
                if (condition && nativeAd != null) {
                    return true
                }
                return false
            }

            fun showAds(activity: Activity?, container: FrameLayout, isReload: Boolean = true) {
                if (canShowAds()) {
                    populateNativeAdView(activity, container)
                    if (isReload) {
                        loadAds(activity)
                    }
                }
            }

            private fun populateNativeAdView(activity: Activity?, container: FrameLayout) {
                if (activity != null && canShowAds()) {
                    try {
                        val adView = LayoutInflater.from(activity)
                            .inflate(layoutCustom, null) as NativeAdView
                        Admob.getInstance()
                            .populateUnifiedNativeAdView(nativeAd!!.admobNativeAd, adView)
                        container.removeAllViews()
                        container.post {
                            container.requestFocus()
                            container.addView(adView)
                        }
                        Log.d(TAG, "populateNativeAdView: success id(${idAds})")
                    } catch (ex: Exception) {
                        Log.d(TAG, "populateNativeAdView: fail id(${idAds})")
                        status = Status.FAIL
                        ex.printStackTrace()
                    } finally {
                    }
                } else {
                    Log.d(TAG, "NativeAds populateNativeAdView: Show fail")
                }
            }

            fun loadAds(activity: Activity?, listener: AperoAdCallback? = null) {
                if (condition && isFailOrShownOrNoneAds() && App.app.isNetworkConnected()) {
                    loadNativeReloadIfFail(activity, reloadTime, listener)
                }
            }

            private fun loadNativeReloadIfFail(
                activity: Activity?, timeReload: Int, listener: AperoAdCallback?
            ) {
                if (timeReload > 0) {
                    status = Status.LOADING
                    AperoAd.getInstance().loadNativeAdResultCallback(
                        activity,
                        idAds,
                        layoutCustom,
                        object : AperoAdCallback() {
                            override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                                super.onNativeAdLoaded(nativeAd)
                                this@NativeLoader.nativeAd = nativeAd
                                status = Status.SUCCESS
                                status = Status.FAIL
                                Log.d(
                                    TAG,
                                    "loadNative: success times(${reloadTime - timeReload + 1}) id(${idAds})"
                                )
                                listener?.onNativeAdLoaded(nativeAd)
                            }

                            override fun onAdFailedToLoad(adError: ApAdError?) {
                                super.onAdFailedToLoad(adError)
                                Log.d(
                                    TAG,
                                    "loadNative: fail times(${reloadTime - timeReload + 1}) id(${idAds})"
                                )
                                if (timeReload - 1 > 0) {
                                    loadNativeReloadIfFail(activity, timeReload, listener)
                                } else {
                                    status = Status.FAIL
                                    listener?.onAdFailedToLoad(adError)
                                }
                            }
                        })
                }
            }
        }

        open class NativeLoaderListener : AperoAdCallback() {
            open fun onNativeFloorLoaded(nativeLoader: NativeLoader) {

            }
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
                                Log.d(
                                    TAG, "inter onAdFailedToShow: idAdsPriority $idAdsPriority"
                                )
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
                                Log.d(
                                    TAG, "forceShowInterstitial: idAdsPriority $idAdsPriority"
                                )
                            }


                            override fun onAdFailedToShow(adError: ApAdError?) {
                                super.onAdFailedToShow(adError)
                                Log.d(
                                    TAG, "inter onAdFailedToShow: idAdsPriority $idAdsPriority"
                                )

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
                            Log.d(
                                TAG, "onInterstitialLoad: idAdsPriority SUCCESS $idAdsPriority"
                            )
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