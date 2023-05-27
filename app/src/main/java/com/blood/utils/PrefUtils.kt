package com.blood.utils

import android.content.SharedPreferences
import com.blood.common.Constant
import com.blood.data.Profile
import com.blood.db.entity.ProfileEntity
import com.google.gson.Gson
import java.util.*
import javax.inject.Inject

class PrefUtils @Inject constructor(private val preferenceHelper: SharedPreferences) {
    companion object {
        const val PREF_NAME = "blood_pressure_pref"

        const val KEY_LANGUAGE_FIRST_OPEN = "KEY_LANGUAGE_FIRST_OPEN"
        const val KEY_ONBOARDING_FIRST_OPEN = "KEY_ONBOARDING_FIRST_OPEN"
        const val KE_DEFAULT_LANGUAGE = "KE_DEFAULT_LANGUAGE"
        const val KE_CURRENT_PROFILE = "KE_CURRENT_PROFILE"
        const val KE_MUST_SHOW_INTER_INFO = "KE_MUST_SHOW_INTER_INFO"
        const val KE_MUST_SHOW_INTER_DETAIL = "KE_MUST_SHOW_INTER_DETAIL"

        // Key remote
        const val REMOTE_SHOW_APPOPEN_RESUME = "appopen_resume"
        const val REMOTE_SHOW_APPOPEN_SPLASH = "appopen_splash"
        const val REMOTE_SHOW_BANNER_HOME = "banner_home"
        const val REMOTE_SHOW_INTER_BLOODPRESSURE_DETAILS = "inter_bloodpressure_details"
        const val REMOTE_SHOW_INTER_BLOODPRESSURE_DETAILS_HIGH = "inter_bloodpressure_details_high"
        const val REMOTE_SHOW_INTER_INFO = "inter_info"
        const val REMOTE_SHOW_INTER_INFO_HIGH = "inter_info_high"
        const val REMOTE_SHOW_INTER_MEASURE = "inter_measure"
        const val REMOTE_SHOW_INTER_MEASURE_HIGH = "inter_measure_high"
        const val REMOTE_SHOW_INTER_SAVE = "inter_save"
        const val REMOTE_SHOW_INTER_SAVE_HIGH = "inter_save_high"
        const val REMOTE_SHOW_INTER_SPLASH = "inter_splash"
        const val REMOTE_SHOW_NATIVE_LANGUAGE = "native_language"
        const val REMOTE_SHOW_NATIVE_ONBOARDING = "native_onboarding"
        const val REMOTE_SHOW_NATIVE_RECENT_ACTION = "native_recent_action"
        const val REMOTE_SHOW_NATIVE_BLOOD_PRESSURE = "native_bloodpressure"
        const val REMOTE_SHOW_BANNER_CREATE_USER = "banner_create_user"
    }

    var profile: Profile?
        get() {
            val profileStr = preferenceHelper.getString(KE_CURRENT_PROFILE, null)
            return if (profileStr != null) {
                Gson().fromJson(profileStr, Profile::class.java)
            } else {
                null
            }
        }
        set(value) {
            val profileStr = if (value != null) Gson().toJson(value) else null
            preferenceHelper?.edit()?.putString(KE_CURRENT_PROFILE, profileStr)?.apply()
        }

    var defaultLanguage: String
        get() = preferenceHelper?.getString(KE_DEFAULT_LANGUAGE, Constant.LANGUAGE_EN)
            ?: Constant.LANGUAGE_EN
        set(value) {
            preferenceHelper?.edit()?.putString(KE_DEFAULT_LANGUAGE, value)?.apply()
        }

    var isShowLanguageFirstOpen: Boolean
        get() = preferenceHelper?.getBoolean(KEY_LANGUAGE_FIRST_OPEN, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(KEY_LANGUAGE_FIRST_OPEN, value)?.apply()
        }

    var isShowOnBoardingFirstOpen: Boolean
        get() = preferenceHelper?.getBoolean(KEY_ONBOARDING_FIRST_OPEN, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(KEY_ONBOARDING_FIRST_OPEN, value)?.apply()
        }

    var mustShowInterInfo: Boolean
        get() = preferenceHelper?.getBoolean(KE_MUST_SHOW_INTER_INFO, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(KE_MUST_SHOW_INTER_INFO, value)?.apply()
        }

    var mustShowInterDetail: Boolean
        get() = preferenceHelper?.getBoolean(KE_MUST_SHOW_INTER_DETAIL, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(KE_MUST_SHOW_INTER_DETAIL, value)?.apply()
        }

    var isShowAppOpenResume: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_APPOPEN_RESUME, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_APPOPEN_RESUME, value)?.apply()
        }

    var isShowAppOpenSplash: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_APPOPEN_SPLASH, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_APPOPEN_SPLASH, value)?.apply()
        }

    var isShowBannerHome: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_BANNER_HOME, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_BANNER_HOME, value)?.apply()
        }

    var isShowInterBloodPressureDetails: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_INTER_BLOODPRESSURE_DETAILS, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_INTER_BLOODPRESSURE_DETAILS, value)
                ?.apply()
        }

    var isShowInterBloodPressureDetailsHigh: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_INTER_BLOODPRESSURE_DETAILS_HIGH, true)
            ?: true
        set(value) {
            preferenceHelper?.edit()
                ?.putBoolean(REMOTE_SHOW_INTER_BLOODPRESSURE_DETAILS_HIGH, value)?.apply()
        }

    var isShowInterInfo: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_INTER_INFO, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_INTER_INFO, value)?.apply()
        }

    var isShowInterInfoHigh: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_INTER_INFO_HIGH, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_INTER_INFO_HIGH, value)?.apply()
        }

    var isShowInterMeasure: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_INTER_MEASURE, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_INTER_MEASURE, value)?.apply()
        }

    var isShowInterMeasureHigh: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_INTER_MEASURE_HIGH, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_INTER_MEASURE_HIGH, value)?.apply()
        }

    var isShowInterSave: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_INTER_SAVE, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_INTER_SAVE, value)?.apply()
        }

    var isShowInterSaveHigh: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_INTER_SAVE_HIGH, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_INTER_SAVE_HIGH, value)?.apply()
        }

    var isShowInterSplash: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_INTER_SPLASH, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_INTER_SPLASH, value)?.apply()
        }

    var isShowNativeLanguage: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_NATIVE_LANGUAGE, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_NATIVE_LANGUAGE, value)?.apply()
        }

    var isShowNativeOnBoarding: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_NATIVE_ONBOARDING, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_NATIVE_ONBOARDING, value)?.apply()
        }

    var isShowNativeRecentAction: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_NATIVE_RECENT_ACTION, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_NATIVE_RECENT_ACTION, value)?.apply()
        }

    var isShowNativeBloodPressure: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_NATIVE_BLOOD_PRESSURE, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_NATIVE_BLOOD_PRESSURE, value)?.apply()
        }

    var isShowBannerCreateUser: Boolean
        get() = preferenceHelper?.getBoolean(REMOTE_SHOW_BANNER_CREATE_USER, true) ?: true
        set(value) {
            preferenceHelper?.edit()?.putBoolean(REMOTE_SHOW_BANNER_CREATE_USER, value)?.apply()
        }
}