package com.fdhasna21.githubusers.repository

import android.content.Context
import android.widget.Toast
import com.fdhasna21.githubusers.model.DbHandlerResult
import com.fdhasna21.githubusers.service.BaseAPI
import com.fdhasna21.githubusers.service.RestClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

abstract class BaseRepository {
    abstract val context : Context
    abstract val apiService : BaseAPI

    inline fun <reified api> serverAPI() : api {
        return RestClient(context)
            .getRetrofit()
            .create(api::class.java)
    }

    inline fun <T> remoteDbHandler(
        crossinline apiCall: () -> Call<T>,
        crossinline onSuccess: (DbHandlerResult.Success<T>) -> Unit,
    ) {
        apiCall().enqueue(object : Callback<T>{
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if(response.isSuccessful){
                    response.body()?.let {
                        onSuccess(DbHandlerResult.Success(it))
                    } ?: onFailed(DbHandlerResult.Error<T>(IOException("Empty Response Body")))
                } else {
                    onFailed(DbHandlerResult.Error<T>(IOException("Error Code: ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onFailed(DbHandlerResult.Error<T>(IOException("Network Failure: ${t.message}")))
            }

        })
    }

    inline suspend fun <T> localDbHandler(
        crossinline databaseCall: suspend () -> T
    ) : DbHandlerResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                DbHandlerResult.Success(databaseCall())
            } catch (exception: Exception) {
                DbHandlerResult.Error(Exception("Database Error: ${exception.message}"))
            }
        }
    }

    fun <T> onFailed(error: DbHandlerResult.Error<T>){
        Toast.makeText(context, error.exception.message, Toast.LENGTH_SHORT).show()
    }
}