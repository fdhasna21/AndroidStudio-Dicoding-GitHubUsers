package com.fdhasna21.githubusers.service.api

import com.fdhasna21.githubusers.model.response.AllUsersResponse
import com.fdhasna21.githubusers.model.response.RepoResponse
import com.fdhasna21.githubusers.model.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Fernanda Hasna on 26/09/2024.
 */

interface UserAPI : BaseAPI {
    @GET("search/users")
    fun getUsersByKeyword(@Query("q") keyword:String,
                          @Query("per_page") per_page:Int=15,
                          @Query("page") page:Int=1):Call<AllUsersResponse>

    @GET("users")
    fun getUsers(@Query("per_page") per_page:Int=15,
                 @Query("since") lastID:Int
    ):Call<ArrayList<UserResponse>>

    @GET("users/{username}") //an user
    fun getUser(@Path("username") username:String) : Call<UserResponse>

    @GET("users/{username}/following")
    fun getUserFollowings(@Path("username") username:String):Call<ArrayList<UserResponse>>

    @GET("users/{username}/followers")
    fun getUserFollowers(@Path("username") username:String):Call<ArrayList<UserResponse>>

    @GET("users/{username}/repos")
    fun getUserRepos(@Path("username") username: String):Call<ArrayList<RepoResponse>>

    @GET("users/{username}/starred")
    fun getUserStars(@Path("username") username: String):Call<ArrayList<RepoResponse>>
}

interface BaseAPI {

}