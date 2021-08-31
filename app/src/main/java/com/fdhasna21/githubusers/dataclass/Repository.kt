package com.fdhasna21.githubusers.dataclass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repository(
    @SerializedName("stargazers_count") val star_count : Long?=0,
    @SerializedName("watchers_count") val watcher_count : Long?=0,
    @SerializedName("name") val repo_name:String?,
    val owner:User?,
    val description:String?,
    val language:String?
    ) : Parcelable
