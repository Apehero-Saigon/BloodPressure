package com.blood.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object FirebaseUtils {

    private var fbAnalytics: FirebaseAnalytics? = null
    fun init(context: Context?) {
        fbAnalytics = FirebaseAnalytics.getInstance(context!!)
    }

    private const val SPLASH_SCREEN = "splash_screen"
    private const val SCR_ONBOARD_1 = "scr_onboard_1"
    private const val SCR_ONBOARD_2 = "scr_onboard_2"
    private const val SCR_ONBOARD_3 = "scr_onboard_3"
    private const val CLICK_START_NOW_SCR_ONBOARD = "click_start_now_scr_onboard"
    private const val SCR_LANGUAGE = "scr_language"
    private const val CLICK_CHOOSE_SCR_LANGUAGE = "click_choose_scr_language"
    private const val SCR_CREATE_USER = "scr_create_user"
    private const val CLICK_CREATE_SCR_CREATE_USER = "click_create_scr_create_user"
    private const val SCR_WELCOME = "scr_welcome"
    private const val CLICK_MEASURE_SCR_WELCOME = "click_measure_scr_welcome"
    private const val CLICK_TRACK_NOW_SCR_WELCOME = "click_track_now_scr_welcome"
    private const val CLICK_CALCULATE_NOW_SCR_WELCOME = "click_calculate_now_scr_welcome"
    private const val SCR_TRACK_BLOOD_PRESSURE = "scr_track_blood_pressure"
    private const val CLICK_MEASURE_SCR_TRACK_BLOOD_PRESSURE =
        "click_measure_scr_track_blood_pressure"
    private const val SCR_RESULT_BLOOD = "scr_result_blood"
    private const val CLICK_BACK_SCR_RESULT_BLOOD = "click_back_scr_result_blood"
    private const val SCR_DASHBOARD = "scr_dashboard"
    private const val CLICK_BLOOD_PRESSURE_SCR_DASHBOARD = "click_blood_pressure_scr_dashboard"
    private const val CLICK_HEART_RATE_SCR_DASHBOARD = "click_heart_rate_scr_dashboard"
    private const val CLICK_WEIGHT_SCR_DASHBOARD = "click_weight_scr_dashboard"
    private const val CLICK_MORE_DETAIL_SCR_DASHBOARD = "click_more_detail_scr_dashboard"
    private const val CLICK_INSIGHT_SRC_DASHBOARD = "click_insight_src_dashboard"
    private const val CLICK_INFORMATION_SRC_DASHBOARD = "click_information_src_dashboard"
    private const val CLICK_SETTING_SRC_DASHBOARD = "click_setting_src_dashboard"
    private const val SCR_INSIGHT = "scr_insight"
    private const val CLICK_BLOOD_PRESSURE_SCR_INSIGHT = "click_blood_pressure_scr_insight"
    private const val CLICK_HEART_RATE_SCR_INSIGHT = "click_heart_rate_scr_insight"
    private const val CLICK_WEIGHT_SCR_INSIGHT = "click_weight_scr_insight"
    private const val CLICK_DASHBOARD_SRC_INSIGHT = "click_dashboard_src_insight"
    private const val CLICK_INFORMATION_SRC_INSIGHT = "click_information_src_insight"
    private const val CLICK_SETTING_SRC_INSIGHT = "click_setting_src_insight"
    private const val SCR_INSIGHT_BLOOD = "scr_insight_blood"
    private const val CHOOSE_DATE_SCR_INSIGHT_BLOOD = "choose_date_scr_insight_blood"
    private const val CLICK_RECORD_SCR_INSIGHT_BLOOD = "click_record_scr_insight_blood"
    private const val SCR_VIEW_RESULT_BLOOD = "scr_view_result_blood"
    private const val CLICK_MORE_OPTION_SCR_VIEW_RESULT_BLOOD =
        "click_more_option_scr_view_result_blood"
    private const val CLICK_EDIT_SCR_VIEW_RESULT_BLOOD = "click_edit_scr_view_result_blood"
    private const val CLICK_DELETE_SCR_VIEW_RESULT_BLOOD = "click_delete_scr_view_result_blood"
    private const val CLICK_BACK_SCR_VIEW_RESULT_BLOOD = "click_back_scr_view_result_blood"
    private const val SCR_EDIT_INFO = "scr_edit_info"
    private const val CLICK_UPDATE_SCR_EDIT_INFO = "click_update_scr_edit_info"
    private const val SCR_INFO_KNOW = "scr_info_know"
    private const val ENTER_SEARCH_SCR_INFO_KNOW = "enter_search_scr_info_know"
    private const val CLICK_TITLE_SCR_INFO_KNOW = "click_title_scr_info_know"
    private const val SCR_VIEW_INFO_KNOW = "scr_view_info_know"
    private const val CLICK_RELATED_POST_SCR_VIEW_INFO_KNOW =
        "click_related_post_scr_view_info_know"
    private const val CLICK_BACK_SCR_VIEW_INFO_KNOW = "click_back_scr_view_info_know"
    private const val CLICK_BACK_SCR_HOME = "scr_home"
    private const val CLICK_ADD_SCR_HOME = "click_add_scr_home"
    private const val POP_UP_CONFIRM = "pop_up_confirm"
    private const val CLICK_SAVE_POP_UP_CONFIRM = "click_save_pop_up_confirm"
    private const val CLICK_CANCEL_POP_UP_CONFIRM = "click_cancel_pop_up_confirm"

    fun eventSplashScreen() {
        fbAnalytics?.logEvent(SPLASH_SCREEN, null)
    }

    fun eventDisplayOnBoarding1() {
        fbAnalytics?.logEvent(SCR_ONBOARD_1, null)
    }

    fun eventDisplayOnBoarding2() {
        fbAnalytics?.logEvent(SCR_ONBOARD_2, null)
    }

    fun eventDisplayOnBoarding3() {
        fbAnalytics?.logEvent(SCR_ONBOARD_3, null)
    }

    fun eventClickStartNowOnBoard() {
        fbAnalytics?.logEvent(CLICK_START_NOW_SCR_ONBOARD, null)
    }

    fun eventDisplayLanguageScreen() {
        fbAnalytics?.logEvent(SCR_LANGUAGE, null)
    }

    fun eventDisplayHomeScreen() {
        fbAnalytics?.logEvent(CLICK_BACK_SCR_HOME, null)
    }

    fun eventClickAddBloodHomeScreen() {
        fbAnalytics?.logEvent(CLICK_ADD_SCR_HOME, null)
    }

    fun eventDisplayConfirmPopup() {
        fbAnalytics?.logEvent(POP_UP_CONFIRM, null)
    }

    fun eventCancelConfirmPopup() {
        fbAnalytics?.logEvent(CLICK_SAVE_POP_UP_CONFIRM, null)
    }

    fun eventYesConfirmPopup() {
        fbAnalytics?.logEvent(CLICK_CANCEL_POP_UP_CONFIRM, null)
    }

    fun eventClickChooseLanguage() {
        fbAnalytics?.logEvent(CLICK_CHOOSE_SCR_LANGUAGE, null)
    }

    fun eventDisplayCreateUser() {
        fbAnalytics?.logEvent(SCR_CREATE_USER, null)
    }

    fun eventClickCreateUser(year: Int, gender: String) {
        val bundle = Bundle()
        bundle.putInt("year_of_birth", year)
        bundle.putString("gender", gender)
        fbAnalytics?.logEvent(CLICK_CREATE_SCR_CREATE_USER, bundle)
    }

    fun eventAppDisplaysWelcomeScreen() {
        fbAnalytics?.logEvent(SCR_WELCOME, null)
    }

    fun eventClickWelcomeMeasure() {
        fbAnalytics?.logEvent(CLICK_MEASURE_SCR_WELCOME, null)
    }

    fun eventClickWelcomeTrackNow() {
        fbAnalytics?.logEvent(CLICK_TRACK_NOW_SCR_WELCOME, null)
    }

    fun eventClickWelcomeCalculator() {
        fbAnalytics?.logEvent(CLICK_CALCULATE_NOW_SCR_WELCOME, null)
    }

    fun eventDisplayAddBloodPressure() {
        fbAnalytics?.logEvent(SCR_TRACK_BLOOD_PRESSURE, null)
    }

    fun eventClickSaveBloodPressure(sys: Int, dia: Int, pulse: Int, status: String) {
        val bundle = Bundle()
        bundle.putInt("sys", sys)
        bundle.putInt("dia", dia)
        bundle.putInt("pulse", pulse)
        bundle.putString("status", status)
        fbAnalytics?.logEvent(CLICK_MEASURE_SCR_TRACK_BLOOD_PRESSURE, bundle)
    }

    fun eventDisplayBloodPressureResult() {
        fbAnalytics?.logEvent(SCR_RESULT_BLOOD, null)
    }

    fun eventClickBackDisplayBloodPressureResult() {
        fbAnalytics?.logEvent(CLICK_BACK_SCR_RESULT_BLOOD, null)
    }

    fun eventDisplayDashBoardScreen() {
        fbAnalytics?.logEvent(SCR_DASHBOARD, null)
    }

    fun eventClickDashBoardBloodPressure() {
        fbAnalytics?.logEvent(CLICK_BLOOD_PRESSURE_SCR_DASHBOARD, null)
    }

    fun eventClickDashBoardHeartRate() {
        fbAnalytics?.logEvent(CLICK_HEART_RATE_SCR_DASHBOARD, null)
    }

    fun eventClickDashBoardWeightBMI() {
        fbAnalytics?.logEvent(CLICK_WEIGHT_SCR_DASHBOARD, null)
    }

    fun eventClickDashBoardMoreDetail() {
        fbAnalytics?.logEvent(CLICK_MORE_DETAIL_SCR_DASHBOARD, null)
    }

    fun eventDisplayInsightScreen() {
        fbAnalytics?.logEvent(SCR_INSIGHT, null)
    }

    fun eventClickInsightBlood() {
        fbAnalytics?.logEvent(CLICK_BLOOD_PRESSURE_SCR_INSIGHT, null)
    }

    fun eventClickInsightHeart() {
        fbAnalytics?.logEvent(CLICK_HEART_RATE_SCR_INSIGHT, null)
    }

    fun eventClickInsightWeightBMI() {
        fbAnalytics?.logEvent(CLICK_WEIGHT_SCR_INSIGHT, null)
    }

    fun eventDisplayInsightBlood() {
        fbAnalytics?.logEvent(SCR_INSIGHT_BLOOD, null)
    }

    fun eventClickChooseDateInsightBlood() {
        fbAnalytics?.logEvent(CHOOSE_DATE_SCR_INSIGHT_BLOOD, null)
    }

    fun eventClickDetailItemInsightBlood() {
        fbAnalytics?.logEvent(CLICK_RECORD_SCR_INSIGHT_BLOOD, null)
    }

    fun eventDisplayBloodDetail() {
        fbAnalytics?.logEvent(SCR_VIEW_RESULT_BLOOD, null)
    }

    fun eventClickBloodDetailOption() {
        fbAnalytics?.logEvent(CLICK_MORE_OPTION_SCR_VIEW_RESULT_BLOOD, null)
    }

    fun eventClickBloodDetailEdit() {
        fbAnalytics?.logEvent(CLICK_EDIT_SCR_VIEW_RESULT_BLOOD, null)
    }

    fun eventClickBloodDetailDelete() {
        fbAnalytics?.logEvent(CLICK_DELETE_SCR_VIEW_RESULT_BLOOD, null)
    }

    fun eventClickBloodDetailBack() {
        fbAnalytics?.logEvent(CLICK_BACK_SCR_VIEW_RESULT_BLOOD, null)
    }

    fun eventDisplayEditBloodDetail() {
        fbAnalytics?.logEvent(SCR_EDIT_INFO, null)
    }

    fun eventClickEditBloodDetailUpdate() {
        fbAnalytics?.logEvent(CLICK_UPDATE_SCR_EDIT_INFO, null)
    }

    fun eventDisplayInfoScreen() {
        fbAnalytics?.logEvent(SCR_INFO_KNOW, null)
    }

    fun eventEnterSearchNameInfo(searchText: String) {
        val bundle = Bundle()
        bundle.putString("search_name", searchText)
        fbAnalytics?.logEvent(ENTER_SEARCH_SCR_INFO_KNOW, bundle)
    }

    fun eventClickInfoDetail() {
        fbAnalytics?.logEvent(CLICK_TITLE_SCR_INFO_KNOW, null)
    }

    fun eventDisplayInfoDetail() {
        fbAnalytics?.logEvent(SCR_VIEW_INFO_KNOW, null)
    }

    fun eventClickRelativeInfoPost() {
        fbAnalytics?.logEvent(CLICK_RELATED_POST_SCR_VIEW_INFO_KNOW, null)
    }

    fun eventClickBackInfoPost() {
        fbAnalytics?.logEvent(CLICK_BACK_SCR_VIEW_INFO_KNOW, null)
    }
}