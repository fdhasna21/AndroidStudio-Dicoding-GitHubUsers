package com.fdhasna21.githubusers.repository

import android.content.Context
import com.fdhasna21.githubusers.model.DataResult
import com.fdhasna21.githubusers.model.response.AllUsersResponse
import com.fdhasna21.githubusers.model.response.RepoResponse
import com.fdhasna21.githubusers.model.response.UserResponse
import com.fdhasna21.githubusers.service.BaseDao
import com.fdhasna21.githubusers.service.api.UserAPI

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

class UserRepositoryImp(override var context: Context) : BaseRepository<UserAPI, BaseDao<*>>(), UserRepository {

    override val apiService = serverAPI<UserAPI>()
    override val daoService = null

    override fun getUsersByKeyword(
        key: String,
        page: Int,
        onSuccess: (DataResult.Success<AllUsersResponse>) -> Unit,
        onFailed: (String?) -> Unit
    ) {
        remoteDbHandler(
            apiCall = {
                apiService.getUsersByKeyword(
                    keyword = key,
                    page = page)
            },
            onSuccess = { result ->
                result.data.let {
                    onSuccess(result)
                }
            },
            onFailed = {
                onFailed(it.exception.message)
                onFailedHandler(it)
            }
        )
    }

    override fun getUsers(
        lastID: Int,
        onSuccess: (DataResult.Success<ArrayList<UserResponse>>) -> Unit,
        onFailed: (String?) -> Unit
    ) {
        remoteDbHandler(
            apiCall = {
                apiService.getUsers(lastID = lastID)
            },
            onSuccess = { result ->
                result.data.let {
                    onSuccess(result)
                }
            },
            onFailed = {
                onFailed(it.exception.message)
                onFailedHandler(it)
            }
        )
    }

    override fun getUserDetail(
        username: String,
        onSuccess: (DataResult.Success<UserResponse>) -> Unit,
        onFailed: (String?) -> Unit
    ) {
        remoteDbHandler(
            apiCall = {
                apiService.getUser(username = username)
            },
            onSuccess = { result ->
                result.data.let {
                    onSuccess(result)
                }
            },
            onFailed = {
                onFailed(it.exception.message)
                onFailedHandler(it)
            }
        )
    }

    override fun getUserFollowings(
        username: String,
        onSuccess: (DataResult.Success<ArrayList<UserResponse>>) -> Unit,
        onFailed: (String?) -> Unit
    ) {
        remoteDbHandler(
            apiCall = {
                apiService.getUserFollowings(username = username)
            },
            onSuccess = { result ->
                result.data.let {
                    onSuccess(result)
                }
            },
            onFailed = {
                onFailed(it.exception.message)
                onFailedHandler(it)
            }
        )
    }

    override fun getUserFollowers(
        username: String,
        onSuccess: (DataResult.Success<ArrayList<UserResponse>>) -> Unit,
        onFailed: (String?) -> Unit
    ) {
        remoteDbHandler(
            apiCall = {
                apiService.getUserFollowers(username = username)
            },
            onSuccess = { result ->
                result.data.let {
                    onSuccess(result)
                }
            },
            onFailed = {
                onFailed(it.exception.message)
                onFailedHandler(it)
            }
        )
    }

    override fun getUserRepos(
        username: String,
        onSuccess: (DataResult.Success<ArrayList<RepoResponse>>) -> Unit,
        onFailed: (String?) -> Unit
    ) {
        remoteDbHandler(
            apiCall = {
                apiService.getUserRepos(username = username)},
            onSuccess = { result ->
                result.data.let {
                    onSuccess(result)
                }
            },
            onFailed = {
                onFailed(it.exception.message)
                onFailedHandler(it)
            }
        )
    }

    override fun getUserStars(
        username: String,
        onSuccess: (DataResult.Success<ArrayList<RepoResponse>>) -> Unit,
        onFailed: (String?) -> Unit
    ) {
        remoteDbHandler(
            apiCall = {
                apiService.getUserStars(username = username)
            },
            onSuccess = { result ->
                result.data.let {
                    onSuccess(result)
                }
            },
            onFailed = {
                onFailed(it.exception.message)
                onFailedHandler(it)
            }
        )
    }
}

interface UserRepository {
    fun getUsersByKeyword(key:String, page:Int, onSuccess: (DataResult.Success<AllUsersResponse>)-> Unit, onFailed: (String?) -> Unit)
    fun getUsers(lastID: Int, onSuccess: (DataResult.Success<ArrayList<UserResponse>>)-> Unit, onFailed: (String?) -> Unit)
    fun getUserDetail(username: String, onSuccess: (DataResult.Success<UserResponse>)-> Unit, onFailed: (String?) -> Unit)
    fun getUserFollowings(username: String, onSuccess: (DataResult.Success<ArrayList<UserResponse>>)-> Unit, onFailed: (String?) -> Unit)
    fun getUserFollowers(username: String, onSuccess: (DataResult.Success<ArrayList<UserResponse>>)-> Unit, onFailed: (String?) -> Unit)
    fun getUserRepos(username: String, onSuccess: (DataResult.Success<ArrayList<RepoResponse>>)-> Unit, onFailed: (String?) -> Unit)
    fun getUserStars(username: String, onSuccess: (DataResult.Success<ArrayList<RepoResponse>>)-> Unit, onFailed: (String?) -> Unit)
}