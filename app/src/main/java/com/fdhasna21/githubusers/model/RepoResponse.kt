package com.fdhasna21.githubusers.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

@Parcelize
data class RepoResponse(
    @SerializedName("stargazers_count") val star_count : Long?=0,
    @SerializedName("watchers_count") val watcher_count : Long?=0,
    @SerializedName("name") val repo_name:String?="",
    val owner: UserResponse?= UserResponse(),
    val description:String?="",
    val language:String?=""
    ) : Parcelable