package com.blood.data

import com.bloodpressure.pressuremonitor.bloodpressuretracker.R

class SettingMenu
constructor(val icon: Int, val name: Int, val type: Type) {
    companion object {

        enum class Type {
            USER, LANGUAGE, PRIVACY, RATE, TERM, DISCLAIMER
        }

        fun getList(): List<SettingMenu> {
            val list = ArrayList<SettingMenu>()

            val user =
                SettingMenu(R.drawable.ic_user_management, R.string.user_management, Type.USER)
            val language = SettingMenu(R.drawable.ic_language, R.string.language, Type.LANGUAGE)
            val privacy = SettingMenu(R.drawable.ic_privacy, R.string.privacy, Type.PRIVACY)
            val rate = SettingMenu(R.drawable.ic_rate_us, R.string.rate_us, Type.RATE)
            val term =
                SettingMenu(R.drawable.ic_term_of_service, R.string.term_of_service, Type.TERM)
            val disclaimer =
                SettingMenu(R.drawable.ic_disclaimer, R.string.disclaimer, Type.DISCLAIMER)

            list.add(language)
            list.add(privacy)
            list.add(term)
            list.add(disclaimer)
            return list
        }
    }
}