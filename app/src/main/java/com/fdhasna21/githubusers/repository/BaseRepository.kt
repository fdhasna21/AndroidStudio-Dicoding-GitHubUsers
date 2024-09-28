package com.fdhasna21.githubusers.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.model.DataResult
import com.fdhasna21.githubusers.service.BaseAPI
import com.fdhasna21.githubusers.service.BaseDao
import com.fdhasna21.githubusers.service.RetrofitService
import com.fdhasna21.githubusers.service.RoomService
import com.fdhasna21.githubusers.utility.ResponseCode
import com.fdhasna21.githubusers.utility.ext.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

/**
 * Updated by Fernanda Hasna on 28/09/2024.
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
        crossinline onFailed: (DataResult.Error<T>) -> Unit
    ) {
        apiCall().enqueue(object : Callback<T>{
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if(response.isSuccessful){
                    response.body()?.let {
                        onSuccess(DataResult.Success(it))
                    } ?: onFailed(DataResult.Error<T>(IOException("Empty Response Body")))
                } else {
                    onFailed(DataResult.Error<T>(IOException("${response.code()} : ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                when (t) {
                    is HttpException -> {
                        onFailed(DataResult.Error<T>(IOException(context.getString(R.string.server_down))))
                    }

                    is UnknownHostException -> {
                        onFailed(DataResult.Error<T>(IOException(context.getString(R.string.no_internet_connection))))
                    }
                    else -> {
                        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
                        if (isConnected) {
                            Log.d(TAG, "Internet available")
                            onFailed(DataResult.Error<T>(IOException(context.getString(R.string.server_down))))
                        } else {
                            Log.d(TAG, "Internet not available")
                            onFailed(DataResult.Error<T>(IOException(context.getString(R.string.no_internet_connection))))
                        }
                    }
                }
            }

        })
    }

    suspend inline fun <T> localDbHandler(
        process: String,
        crossinline databaseCall: suspend () -> T,
        crossinline onSuccess: (T) -> Unit,
        crossinline onFailed: (DataResult.Error<T>) -> Unit
    ) {
        when (val result = withContext(Dispatchers.IO) {
            try {
                DataResult.Success(databaseCall())
            } catch (e: Exception) {
                DataResult.Error(Exception("${ResponseCode.ROOM_ERROR} : ${e.message}"))
            }
        }) {
            is DataResult.Success -> {
                Log.d(TAG, "$process success : ${result.data.toString()}")
                onSuccess(result.data)
            }
            is DataResult.Error -> onFailed(result)
        }
    }

    fun <T> onFailedHandler(error: DataResult.Error<T>){
        Log.e(TAG, "FailedHandler", error.exception)
        Toast.makeText(context, error.exception.message, Toast.LENGTH_SHORT).show()
    }
}