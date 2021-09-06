package com.fdhasna21.githubusers.server

import com.fdhasna21.githubusers.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServerAPI {
    private var retrofit : Retrofit? = null
    private var httpClient = OkHttpClient.Builder()

    fun getServerAPI() : Retrofit{
        if(retrofit == null){
            httpClient.addInterceptor(Interceptor { chain ->
                val original: Request = chain.request()

                val request = original.newBuilder()
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("Authorization", "client_id ${BuildConfig.GITHUB_CLIENT_ID}")
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            })

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val client = httpClient.build()
            retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(BuildConfig.GITHUB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit!!
    }
}