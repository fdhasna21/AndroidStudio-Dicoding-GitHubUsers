package com.fdhasna21.githubusers.model.response

/**
 * Created by Fernanda Hasna on 24/09/2024.
 */

sealed class DbHandlerResult<out T> {
    data class Success<out T>(val data: T) : DbHandlerResult<T>()
    data class Error<out T>(val exception: Exception) : DbHandlerResult<T>()
    object Loading : DbHandlerResult<Nothing>()
}