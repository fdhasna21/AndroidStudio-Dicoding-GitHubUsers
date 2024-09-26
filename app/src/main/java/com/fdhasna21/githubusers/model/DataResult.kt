package com.fdhasna21.githubusers.model

/**
 * Created by Fernanda Hasna on 24/09/2024.
 * Updated by Fernanda Hasna on 26/09/2024.
 */

sealed class DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error<out T>(val exception: Exception) : DataResult<T>()
//    object Loading : DbHandlerResult<Nothing>()
}