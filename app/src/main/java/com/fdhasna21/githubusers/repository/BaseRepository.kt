package com.fdhasna21.githubusers.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.fdhasna21.githubusers.model.DataResult
import com.fdhasna21.githubusers.service.BaseAPI
import com.fdhasna21.githubusers.service.BaseDao
import com.fdhasna21.githubusers.service.RetrofitService
import com.fdhasna21.githubusers.service.RoomService
import com.fdhasna21.githubusers.utility.type.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

abstract class BaseRepository<API: BaseAPI?, DAO: BaseDao<*>?> {
    abstract val context : Context
    abstract val apiService : API?
    abstract val daoService : DAO?

    inline fun <reified api : API> serverAPI() : api {
        return RetrofitService(context)
            .getRetrofit()
            .create(api::class.java)
    }

    val roomDb: RoomService by lazy {
        RoomService.getDatabase(context)
    }

    inline fun <T> remoteDbHandler(
        crossinline apiCall: () -> Call<T>,
        crossinline onSuccess: (DataResult.Success<T>) -> Unit,
    ) {
        apiCall().enqueue(object : Callback<T>{
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if(response.isSuccessful){
                    response.body()?.let {
                        onSuccess(DataResult.Success(it))
                    } ?: onFailed(DataResult.Error<T>(IOException("Empty Response Body")))
                } else {
                    onFailed(DataResult.Error<T>(IOException("Error Code: ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onFailed(DataResult.Error<T>(IOException("Network Failure: ${t.message}")))
            }

        })
    }

    suspend inline fun <T> localDbHandler(
        process: String,
        crossinline databaseCall: suspend () -> T,
        crossinline onSuccess: (T) -> Unit
    ) {
        when (val result = withContext(Dispatchers.IO) {
            try {
                DataResult.Success(databaseCall())
            } catch (e: Exception) {
                Log.e(TAG,"Error while accessing database", e )
                DataResult.Error(Exception("Database Error: ${e.message}"))
            }
        }) {
            is DataResult.Success -> {
                Log.d(TAG, "$process success : ${result.data.toString()}")
                onSuccess(result.data)
            }
            is DataResult.Error -> onFailed(result)
        }
    }

    fun <T> onFailed(error: DataResult.Error<T>){
        Toast.makeText(context, error.exception.message, Toast.LENGTH_SHORT).show()
    }
}