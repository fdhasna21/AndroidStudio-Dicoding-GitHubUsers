package com.fdhasna21.githubusers.repository

import android.content.Context
import com.fdhasna21.githubusers.model.AllUsersResponse
import com.fdhasna21.githubusers.model.DbHandlerResult
import com.fdhasna21.githubusers.model.RepoResponse
import com.fdhasna21.githubusers.model.UserResponse
import com.fdhasna21.githubusers.service.api.UserAPI

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

class UserRepositoryImp(override var context: Context) : BaseRepository(), UserRepository {

    override val apiService = serverAPI<UserAPI>()

    override fun getUsersByKeyword(
        key: String,
        page: Int,
        onSuccess: (DbHandlerResult.Success<AllUsersResponse>)->Unit
    ){
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
            }
        )
    }

    override fun getUsers(
        lastID: Int,
        onSuccess: (DbHandlerResult.Success<ArrayList<UserResponse>>)->Unit
    ) {
        remoteDbHandler(
            apiCall = {
                apiService.getUsers(lastID = lastID)
            },
            onSuccess = { result ->
                result.data.let {
                    onSuccess(result)
                }
            }
        )
    }

    override fun getUserDetail(
        username: String,
        onSuccess: (DbHandlerResult.Success<UserResponse>) -> Unit
    ) {
        remoteDbHandler(
            apiCall = {
                apiService.getUser(username = username)
            },
            onSuccess = { result ->
                result.data.let {
                    onSuccess(result)
                }
            }
        )
    }

    override fun getUserFollowings(
        username: String,
        onSuccess: (DbHandlerResult.Success<ArrayList<UserResponse>>) -> Unit
    ) {
        remoteDbHandler(
            apiCall = {
                apiService.getUserFollowings(username = username)
            },
            onSuccess = { result ->
                result.data.let {
                    onSuccess(result)
                }
            }
        )
    }

    override fun getUserFollowers(
        username: String,
        onSuccess: (DbHandlerResult.Success<ArrayList<UserResponse>>) -> Unit
    ) {
        remoteDbHandler(
            apiCall = {
                apiService.getUserFollowers(username = username)
            },
            onSuccess = { result ->
                result.data.let {
                    onSuccess(result)
                }
            }
        )
    }

    override fun getUserRepos(
        username: String,
        onSuccess: (DbHandlerResult.Success<ArrayList<RepoResponse>>) -> Unit
    ) {
        remoteDbHandler(
            apiCall = {
                apiService.getUserRepos(username = username)},
            onSuccess = { result ->
                result.data.let {
                    onSuccess(result)
                }
            }
        )
    }

    override fun getUserStars(
        username: String,
        onSuccess: (DbHandlerResult.Success<ArrayList<RepoResponse>>) -> Unit
    ) {
        remoteDbHandler(
            apiCall = {
                apiService.getUserStars(username = username)
            },
            onSuccess = { result ->
                result.data.let {
                    onSuccess(result)
                }
            }
        )
    }
}

interface UserRepository {
    fun getUsersByKeyword(key:String, page:Int, onSuccess: (DbHandlerResult.Success<AllUsersResponse>)->Unit)
    fun getUsers(lastID: Int, onSuccess: (DbHandlerResult.Success<ArrayList<UserResponse>>)->Unit)
    fun getUserDetail(username: String, onSuccess: (DbHandlerResult.Success<UserResponse>)->Unit)
    fun getUserFollowings(username: String, onSuccess: (DbHandlerResult.Success<ArrayList<UserResponse>>)->Unit)
    fun getUserFollowers(username: String, onSuccess: (DbHandlerResult.Success<ArrayList<UserResponse>>)->Unit)
    fun getUserRepos(username: String, onSuccess: (DbHandlerResult.Success<ArrayList<RepoResponse>>)->Unit)
    fun getUserStars(username: String, onSuccess: (DbHandlerResult.Success<ArrayList<RepoResponse>>)->Unit)
}