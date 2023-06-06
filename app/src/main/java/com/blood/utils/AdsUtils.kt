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
    val interInsightDetail: InterPriority = InterPriority(
        interLoaderHigh = InterPriority.InterstitialLoader(
            BuildConfig.inter_insight_details_high, 3, prefUtils.isShowInterInsightDetailHigh
        ), interLoaderNormal = InterPriority.InterstitialLoader(
            BuildConfig.inter_insight_details, 1, prefUtils.isShowInterInsightDetail
        )
    ).apply {
        showAfterClick = 2
        countShown = -2
    }
    val interSave: InterPriority = InterPriority(
        interLoaderHigh = InterPriority.InterstitialLoader(
            BuildConfig.inter_save_high, 3, prefUtils.isShowInterSaveHigh
        ), interLoaderNormal = InterPriority.InterstitialLoader(
            BuildConfig.inter_save, 1, prefUtils.isShowInterSave
        )
    ).apply {
        showAfterClick = 2
    }
    val interInfo: InterPriority = InterPriority(
        interLoaderHigh = InterPriority.InterstitialLoader(
            BuildConfig.inter_info_high, 3, prefUtils.isShowInterInfoHigh
        ), interLoaderNormal = InterPriority.InterstitialLoader(
            BuildConfig.inter_info, 1, prefUtils.isShowInterInfo
        )
    )

    // native
    val nativeOnBoarding = NativeAds(
        R.layout.native_medium, nativeLoaderHigh = NativeAds.NativeLoader(
            BuildConfig.native_onboarding_high, 3, prefUtils.isShowNativeOnBoarding
        ), nativeLoaderNormal = NativeAds.NativeLoader(
            BuildConfig.native_onboarding, 1, prefUtils.isShowNativeOnBoarding
        )
    )
    val nativeCreateUser = NativeAds(
        R.layout.layout_native_medium_custom, nativeLoaderHigh = NativeAds.NativeLoader(
            BuildConfig.native_create_user_high, 3, prefUtils.isShowNativeCreateUser
        ), nativeLoaderNormal = NativeAds.NativeLoader(
            BuildConfig.native_create_user, 1, prefUtils.isShowNativeCreateUser
        )
    )
    val nativeBloodPressure = NativeAds(
        R.layout.layout_native_medium_custom, nativeLoaderHigh = NativeAds.NativeLoader(
            BuildConfig.native_bloodpressure, 1, prefUtils.isShowNativeBloodPressure
        )
    )
    val nativeDefaultValue = NativeAds(
        R.layout.native_medium, nativeLoaderHigh = NativeAds.NativeLoader(
            BuildConfig.native_value_high, 3, prefUtils.isShowNativeDefaultValue
        ), nativeLoaderNormal = NativeAds.NativeLoader(
            BuildConfig.native_value, 1, prefUtils.isShowNativeDefaultValue
        )
    )
    val nativeLanguage = NativeAds(
        R.layout.native_medium, nativeLoaderHigh = NativeAds.NativeLoader(
            BuildConfig.native_language_high, 3, prefUtils.isShowNativeLanguage
        ), nativeLoaderNormal = NativeAds.NativeLoader(
            BuildConfig.native_language, 1, prefUtils.isShowNativeLanguage
        )
    )
    val nativeExit = NativeAds(
        R.layout.native_medium, nativeLoaderHigh = NativeAds.NativeLoader(
            BuildConfig.native_exit_high, 3, prefUtils.isShowNativeExitHigh
        ), nativeLoaderNormal = NativeAds.NativeLoader(
            BuildConfig.native_exit, 1, prefUtils.isShowNativeExit
        )
    )
    val nativeRecentAction = NativeAds(
        R.layout.native_medium, nativeLoaderHigh = NativeAds.NativeLoader(
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
                        status = Status.SHOWN
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
                if (condition && isFailOrShownOrNoneAds() && App.app.isNetworkConnected() && !isLoading()) {
                    loadNativeReloadIfFail(activity, reloadTime, listener)
                }
            }

            private fun loadNativeReloadIfFail(
                activity: Activity?, timeReload: Int, listener: AperoAdCallback?
            ) {
                if (timeReload > 0) {
                    status = Status.LOADING
                    AperoAd.getInstance().loadNativeAdResultCallback(activity,
                        idAds,
                        layoutCustom,
                        object : AperoAdCallback() {
                            override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                                super.onNativeAdLoaded(nativeAd)
                                this@NativeLoader.nativeAd = nativeAd
                                status = Status.SUCCESS
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
                                    loadNativeReloadIfFail(activity, timeReload - 1, listener)
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

    class InterPriority(
        val interLoaderHigh: InterstitialLoader? = null,
        val interLoaderNormal: InterstitialLoader? = null,
        val interLoaderLow: InterstitialLoader? = null,
    ) {
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

        var isShowHighAds: Boolean = true
        var isShowNormalAds: Boolean = true

        private fun canShowAds(): Boolean {
            if (interLoaderHigh?.canShowAds() == true || interLoaderNormal?.canShowAds() == true || interLoaderLow?.canShowAds() == true) {
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
                        loadInterPrioritySameTime(context, null)
                    }
                }
            }
            if (canShowAds() && (!isShowByIntervalTime || checkShowByIntervalTime()) && checkShowBySkip()) {
                if (interLoaderHigh?.canShowAds() == true) {
                    interLoaderHigh.showAds(context, object : AperoAdCallback() {
                        override fun onNextAction() {
                            super.onNextAction()
                            finishCallback(reloadAfterShow)
                        }

                        override fun onAdFailedToShow(adError: ApAdError?) {
                            super.onAdFailedToShow(adError)
                            countShown = 0
                        }
                    })
                } else if (interLoaderNormal?.canShowAds() == true) {
                    interLoaderNormal.showAds(context, object : AperoAdCallback() {
                        override fun onNextAction() {
                            super.onNextAction()
                            finishCallback(reloadAfterShow)
                        }

                        override fun onAdFailedToShow(adError: ApAdError?) {
                            super.onAdFailedToShow(adError)
                            countShown = 0
                        }
                    })
                } else if (interLoaderLow?.canShowAds() == true) {
                    interLoaderLow.showAds(context, object : AperoAdCallback() {
                        override fun onNextAction() {
                            super.onNextAction()
                            finishCallback(reloadAfterShow)
                        }

                        override fun onAdFailedToShow(adError: ApAdError?) {
                            super.onAdFailedToShow(adError)
                            countShown = 0
                        }
                    })
                } else {
                    finishCallback(reloadAfterShow)
                }
            } else {
                finishCallback(reloadAfterShow)
            }
        }

        fun loadInterPrioritySameTime(context: Context, listener: AperoAdCallback? = null) {
            interLoaderHigh?.loadAds(context, object : AperoAdCallback() {
                override fun onInterstitialLoad(interstitialAd: ApInterstitialAd?) {
                    super.onInterstitialLoad(interstitialAd)
                    Log.d(TAG, "inter:load success high ${interLoaderHigh.idAds}")
                    listener?.onInterstitialLoad(interstitialAd)
                }

                override fun onAdFailedToLoad(adError: ApAdError?) {
                    super.onAdFailedToLoad(adError)
                    if (interLoaderNormal?.isLoadSuccess() == false && interLoaderLow?.isLoadSuccess() == false) {
                        listener?.onAdFailedToLoad(adError)
                    }
                }
            })
            interLoaderNormal?.loadAds(context, object : AperoAdCallback() {
                override fun onInterstitialLoad(interstitialAd: ApInterstitialAd?) {
                    super.onInterstitialLoad(interstitialAd)
                    Log.d(TAG, "inter:load success normal ${interLoaderNormal.idAds}")
                    if (interLoaderHigh?.isLoadSuccess() == false) {
                        listener?.onInterstitialLoad(interstitialAd)
                    }
                }

                override fun onAdFailedToLoad(adError: ApAdError?) {
                    super.onAdFailedToLoad(adError)
                    if (interLoaderHigh?.isLoadSuccess() == false && interLoaderLow?.isLoadSuccess() == false) {
                        listener?.onAdFailedToLoad(adError)
                    }
                }
            })
            interLoaderLow?.loadAds(context, object : AperoAdCallback() {
                override fun onInterstitialLoad(interstitialAd: ApInterstitialAd?) {
                    super.onInterstitialLoad(interstitialAd)
                    Log.d(TAG, "inter:load success low ${interLoaderLow.idAds}")
                    if (interLoaderHigh?.isLoadSuccess() == false && interLoaderNormal?.isLoadSuccess() == false) {
                        listener?.onInterstitialLoad(interstitialAd)
                    }
                }

                override fun onAdFailedToLoad(adError: ApAdError?) {
                    super.onAdFailedToLoad(adError)
                    if (interLoaderHigh?.isLoadSuccess() == false && interLoaderNormal?.isLoadSuccess() == false) {
                        listener?.onAdFailedToLoad(adError)
                    }
                }
            })
        }

        class InterstitialLoader(
            val idAds: String, val reloadTime: Int = 1, private val condition: Boolean = true
        ) {

            private var interstitialAd: ApInterstitialAd? = null
            private var status: Status = Status.NONE

            private fun isFailOrShownOrNoneAds() =
                status == Status.FAIL || status == Status.SHOWN || status == Status.NONE

            fun isLoadSuccess() = status == Status.SUCCESS

            fun canShowAds(): Boolean {
                if (condition && interstitialAd != null) {
                    return true
                }
                return false
            }

            fun showAds(context: Context, listener: AperoAdCallback? = null) {
                if (canShowAds() && condition) {
                    AperoAd.getInstance().forceShowInterstitial(
                        context, interstitialAd, object : AperoAdCallback() {
                            override fun onNextAction() {
                                super.onNextAction()
                                lastShowInter = System.currentTimeMillis()
                                status = Status.SHOWN
                                Log.d(TAG, "inter:showAds success $idAds")
                                listener?.onNextAction()
                            }

                            override fun onAdFailedToShow(adError: ApAdError?) {
                                super.onAdFailedToShow(adError)
                                Log.d(TAG, "inter:showAds fail $idAds")
                                listener?.onAdFailedToShow(adError)
                            }
                        }, false
                    )
                } else {
                    listener?.onAdFailedToShow(ApAdError("Fail"))
                }
            }

            fun loadAds(context: Context?, listener: AperoAdCallback? = null) {
                if (condition && isFailOrShownOrNoneAds() && App.app.isNetworkConnected()) {
                    loadAdsReloadIfFail(context, reloadTime, listener)
                }
            }

            private fun loadAdsReloadIfFail(
                context: Context?, timeReload: Int, listener: AperoAdCallback?
            ) {
                if (timeReload > 0) {
                    status = Status.LOADING
                    AperoAd.getInstance()
                        .getInterstitialAds(context, idAds, object : AperoAdCallback() {
                            override fun onInterstitialLoad(interstitialAd: ApInterstitialAd?) {
                                super.onInterstitialLoad(interstitialAd)
                                Log.d(
                                    TAG,
                                    "inter: load SUCCESS times(${reloadTime - timeReload + 1}) $idAds"
                                )
                                this@InterstitialLoader.interstitialAd = interstitialAd
                                listener?.onInterstitialLoad(interstitialAd)
                                status = Status.SUCCESS
                            }

                            override fun onAdFailedToLoad(adError: ApAdError?) {
                                super.onAdFailedToLoad(adError)
                                Log.d(
                                    TAG,
                                    "inter: fail times(${reloadTime - timeReload + 1}) id(${idAds})"
                                )
                                if (timeReload - 1 > 0) {
                                    loadAdsReloadIfFail(context, timeReload - 1, listener)
                                } else {
                                    status = Status.FAIL
                                    listener?.onAdFailedToLoad(adError)
                                }
                            }
                        })
                }
            }

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