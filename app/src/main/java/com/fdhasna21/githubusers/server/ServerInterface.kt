package com.fdhasna21.githubusers.server

import com.fdhasna21.githubusers.resolver.dataclass.Repository
import com.fdhasna21.githubusers.resolver.dataclass.SearchUsers
import com.fdhasna21.githubusers.resolver.dataclass.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerInterface {
    @GET("search/users")
    fun search(@Query("q") keyword:String,
               @Query("per_page") per_page:Int=15,
               @Query("page") page:Int=1):Call<SearchUsers>?

    @GET("users")
    fun users(@Query("per_page") per_page:Int=15,
              @Query("since") lastID:Int
    ):Call<ArrayList<User>>?

    @GET("users/{username}") //an user
    fun user(@Path("username") username:String) : Call<User>?

    @GET("users/{username}/following")
    fun following(@Path("username") username:String):Call<ArrayList<User>>?

    @GET("users/{username}/followers")
    fun followers(@Path("username") username:String):Call<ArrayList<User>>?

    @GET("users/{username}/repos")
    fun repository(@Path("username") username: String):Call<ArrayList<Repository>>?

    @GET("users/{username}/starred")
    fun starred(@Path("username") username: String):Call<ArrayList<Repository>>?
}