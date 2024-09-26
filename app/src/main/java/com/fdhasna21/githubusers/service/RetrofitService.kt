package com.fdhasna21.githubusers.service

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.fdhasna21.githubusers.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */
class RetrofitService(context : Context) {
    fun getRetrofit() : Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.GITHUB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    private val interceptor = Interceptor { chain ->
        val original: Request = chain.request()
        val request = original.newBuilder()
            .header("Accept", "application/vnd.github.v3+json")
            .header("Authorization", "client_id ${BuildConfig.GITHUB_CLIENT_ID}")
            .method(original.method, original.body)
            .build()
        chain.proceed(request)
    }

    private val chuckerInterceptor = ChuckerInterceptor.Builder(context)
        .collector(
            ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR))
        .maxContentLength(250_000L)
        .redactHeaders("Auth-Token", "Bearer")
        .build()

    private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(chuckerInterceptor)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .writeTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .cache(null)
            .build()
}

interface BaseAPI{}