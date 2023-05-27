package com.blood.di.module

import android.os.Environment
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.blood.network.ApiService
import com.blood.network.api.ProfileAPI
import com.blood.network.api.interfaceapi.IProfileAPI
import com.blood.network.interceptor.RequestInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    private val logger: HttpLoggingInterceptor
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                loggingInterceptor.apply { level = HttpLoggingInterceptor.Level.BODY }
            }
            return loggingInterceptor
        }

    @Singleton
    @Provides
    @Named("cached")
    fun provideOkHttpClient(): OkHttpClient {
        val cache = Cache(Environment.getDownloadCacheDirectory(), 10 * 1024 * 1024)
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(RequestInterceptor())
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .cache(cache)
            .build()
    }

    @Singleton
    @Provides
    @Named("non_cached")
    fun provideNonCachedOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(RequestInterceptor())
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(@Named("non_cached") client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit.Builder): ApiService {
        return retrofit.baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideService(apiService: ApiService): IProfileAPI {
        return ProfileAPI(apiService)
    }
}