package com.blood.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import com.applovin.sdk.AppLovinMediationProvider
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkSettings
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bytedance.sdk.openadsdk.api.init.PAGConfig
import com.bytedance.sdk.openadsdk.api.init.PAGSdk
import com.google.ads.mediation.applovin.AppLovinMediationAdapter
import com.google.ads.mediation.mintegral.MintegralMediationAdapter
import com.google.ads.mediation.pangle.PangleMediationAdapter
import com.google.ads.mediation.vungle.VungleMediationAdapter
import com.google.android.gms.ads.*
import com.google.android.gms.ads.initialization.InitializationStatus
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.sdk.InitializationListener
import com.mbridge.msdk.out.MBridgeSDKFactory
import com.mbridge.msdk.out.SDKInitStatusListener
import com.vungle.warren.InitCallback
import com.vungle.warren.Vungle
import com.vungle.warren.error.VungleException

class AdsMediationUtils {


    companion object {
        val MINTEGRAL_APP_ID = "8133025"
        val MINTEGRAL_KEY = "bbcbb5b7173a7c8c07e45c4d4a951683"
        val PANGLE_APP_ID = "8134349"
        val IRON_SOURCE_APP_ID = "1a153fb15"
        val VUNGLE_APP_ID = "645dade6802fa327835903f4"
        val APPLOVIN_APP_ID =
            "3N4Mt8SNhOzkQnGb9oHsRRG1ItybcZDpJWN1fVAHLdRagxP-_k_ZXVaMAdMe5Otsmp6qJSXskfsrtakfRmPAGW"

        fun init(application: Application, listDeviceTest: List<String>) {
            Log.d("MobileAds", "Init MobileAds")
            MobileAds.initialize(application) { initializationStatus: InitializationStatus ->
                val statusMap =
                    initializationStatus.adapterStatusMap
                for (adapterClass in statusMap.keys) {
                    val status = statusMap[adapterClass]
                    Log.d(
                        "MobileAds", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status!!.description, status.latency
                        )
                    )
                }
            }
            val requestConfiguration = RequestConfiguration.Builder()
                .setTestDeviceIds(listDeviceTest)
                .build()
            MobileAds.setRequestConfiguration(requestConfiguration)
            MobileAds.registerRtbAdapter(PangleMediationAdapter::class.java)
            MobileAds.registerRtbAdapter(VungleMediationAdapter::class.java)
            MobileAds.registerRtbAdapter(AppLovinMediationAdapter::class.java)
            MobileAds.registerRtbAdapter(MintegralMediationAdapter::class.java)

            PangleUtils.init(application)
            VungleUtils.init(application)
            AppLovinUtils.init(application, listDeviceTest)
            MintegralUtils.init(application, listDeviceTest)
        }

        fun isDestroy(activity: Activity?): Boolean {
            return activity == null || activity.isFinishing || activity.isDestroyed
        }

        enum class StatusLoad { LOADING, DONE, FAIL, NONE }
    }

    class PangleUtils {

        companion object {
            fun init(context: Context) {
                PAGSdk.init(context, buildNewConfig(), object : PAGSdk.PAGInitCallback {
                    override fun success() {
                        Log.d(PangleUtils::class.java.simpleName, "init success")
                    }

                    override fun fail(code: Int, msg: String) {
                        Log.d(PangleUtils::class.java.simpleName, "init fail $code $msg")
                    }
                })
            }

            private fun buildNewConfig(): PAGConfig? {
                return PAGConfig.Builder()
                    .appId(PANGLE_APP_ID)
                    .debugLog(BuildConfig.build_debug)
                    .supportMultiProcess(true)
                    .build()
            }
        }
    }

    class VungleUtils {

        companion object {
            fun init(context: Context) {
                Vungle.init(VUNGLE_APP_ID, context, object : InitCallback {
                    override fun onSuccess() {
                        Log.d(VungleUtils::class.java.simpleName, "init onSuccess")
                    }

                    override fun onError(exception: VungleException?) {
                        Log.d(VungleUtils::class.java.simpleName, "init onError")
                        exception?.printStackTrace()
                    }

                    override fun onAutoCacheAdAvailable(placementId: String?) {
                        Log.d(
                            VungleUtils::class.java.simpleName,
                            "init onAutoCacheAdAvailable $placementId"
                        )
                    }
                })
                val configuration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .build()
                MobileAds.setRequestConfiguration(configuration)
            }
        }
    }

    class AppLovinUtils {

        companion object {
            fun init(context: Context, listDeviceTest: List<String>) {
                AppLovinSdk.getInstance(
                    APPLOVIN_APP_ID,
                    AppLovinSdkSettings(context).apply {
                    },
                    context
                ).initializeSdk()
                AppLovinSdk.getInstance(context).mediationProvider = AppLovinMediationProvider.MAX
            }
        }
    }

    class MintegralUtils {

        companion object {
            fun init(application: Application, listDeviceTest: List<String>) {
                val sdk = MBridgeSDKFactory.getMBridgeSDK()
                val map = sdk.getMBConfigurationMap(MINTEGRAL_APP_ID, MINTEGRAL_KEY)
                sdk.init(map, application, object : SDKInitStatusListener {
                    override fun onInitSuccess() {
                        Log.d(MintegralUtils::class.java.simpleName, "init onInitSuccess")
                    }

                    override fun onInitFail(p0: String?) {
                        Log.d(MintegralUtils::class.java.simpleName, "init onInitFail")
                    }

                })
            }
        }
    }

    class IronSourceUtils {

        companion object {
            //            add tis in activity use
//            protected void onResume() {
//                super.onResume();
//                IronSource.onResume(this);
//            }
//            protected void onPause() {
//                super.onPause();
//                IronSource.onPause(this);
//            }
            fun init(activity: Activity) {
                IronSource.init(activity, IRON_SOURCE_APP_ID, object : InitializationListener {
                    override fun onInitializationComplete() {
                        Log.d(IronSourceUtils::class.java.simpleName, "init onSuccess")
                    }
                })
            }
        }
    }
}