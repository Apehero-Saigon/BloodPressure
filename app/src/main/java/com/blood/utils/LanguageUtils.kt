package com.blood.utils

import android.content.Context
import android.content.res.Configuration
import com.blood.App
import com.blood.common.Constant
import com.blood.utils.wrapper.ContextWrapper
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.ltl.apero.languageopen.Language
import java.util.Locale

object LanguageUtils {
    private var myLocale: Locale? = null

    fun loadLocale(context: Context, language: String) {
        if (language.isEmpty()) {
            val locale = Locale.getDefault()
            Locale.setDefault(locale)
            ContextWrapper.wrap(context, locale);
        } else {
            changeLanguage(context, language)
        }
    }

    fun changeLanguage(context: Context, lang: String) {
        if (lang.equals("", ignoreCase = true)) return
        myLocale = Locale(lang)
        if (myLocale != null) {
            Locale.setDefault(myLocale!!)
        } else {
            myLocale = Locale.getDefault()
            Locale.setDefault(Locale.getDefault())
        }

        val config = Configuration()
        config.locale = myLocale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        context.applicationContext.resources.updateConfiguration(
            config, context.applicationContext.resources.displayMetrics
        )
    }

    fun languageListItems(context: Context) = mutableListOf(
        Language(
            Constant.LANGUAGE_EN,
            context.getString(R.string.english),
            R.drawable.ic_language_english,
            false
        ), Language(
            Constant.LANGUAGE_DE,
            context.getString(R.string.german),
            R.drawable.ic_language_german,
            false
        ), Language(
            Constant.LANGUAGE_ES,
            context.getString(R.string.spanish),
            R.drawable.ic_language_spanish,
            false
        ), Language(
            Constant.LANGUAGE_FR,
            context.getString(R.string.french),
            R.drawable.ic_language_french,
            false
        ), Language(
            Constant.LANGUAGE_JA,
            context.getString(R.string.japanese),
            R.drawable.ic_language_japanese,
            false
        ), Language(
            Constant.LANGUAGE_PT,
            context.getString(R.string.portugal),
            R.drawable.ic_language_portugal,
            false
        ), Language(
            Constant.LANGUAGE_KO,
            context.getString(R.string.korean),
            R.drawable.ic_language_korea,
            false
        ), Language(
            Constant.LANGUAGE_VI,
            context.getString(R.string.vietnamese),
            R.drawable.ic_language_vietnam,
            false
        )
    )

}