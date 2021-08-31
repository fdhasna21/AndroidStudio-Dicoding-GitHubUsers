package com.fdhasna21.githubusers.server

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fdhasna21.githubusers.R
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServerAPI {
    var BASE_URL : String = "http://api.github.com/"
    var retrofit : Retrofit? = null
    var httpClient = OkHttpClient.Builder()

    fun getServerAPI(progressIndicator: CircularProgressIndicator, activity: AppCompatActivity) : Retrofit?{
        if(retrofit == null){
            progressIndicator.visibility = View.VISIBLE
            httpClient.addInterceptor(Interceptor { chain ->
                val original: Request = chain.request()

                val request = original.newBuilder()
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("Authorization", "client_id ${activity.resources.getString(R.string.client_id)}")
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
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit
    }
}