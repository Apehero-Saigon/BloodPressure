package com.blood.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.blood.App
import com.blood.utils.PrefUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {
    @Provides
    @Singleton
    fun provideContext(app: App): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideSharedPreferences(app: App): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(app)

    @Provides
    @Singleton
    fun providePrefUtils(sharedPreferences: SharedPreferences): PrefUtils =
        PrefUtils(sharedPreferences)
}