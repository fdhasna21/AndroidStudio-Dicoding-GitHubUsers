package com.fdhasna21.githubusers.server

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
 * Updated by Fernanda Hasna on 23/09/2024.
 */
class ServerAPI {
    private var retrofit : Retrofit? = null

    fun getServerAPI() : Retrofit{
        if(retrofit == null){
            val gson = GsonBuilder()
                .setLenient()
                .create()

            retrofit = Retrofit.Builder()
                .client(okHttpClient(interceptor, httpLogging))
                .baseUrl(BuildConfig.GITHUB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit!!
    }

    private val httpLogging: HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            return httpLoggingInterceptor
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

    private fun okHttpClient(
        interceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .writeTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .cache(null)
            .build()
    }
}