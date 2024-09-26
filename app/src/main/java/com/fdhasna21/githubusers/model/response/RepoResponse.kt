package com.fdhasna21.githubusers.model.response

import com.google.gson.annotations.SerializedName

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

data class RepoResponse(
    @SerializedName("stargazers_count") val star_count : Long?=0,
    @SerializedName("watchers_count") val watcher_count : Long?=0,
    @SerializedName("name") val repo_name:String?="",
    val owner: UserResponse?= UserResponse(),
    val description:String?="",
    val language:String?=""
) : BaseResponse()