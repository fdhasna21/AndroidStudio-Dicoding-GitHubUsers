package com.fdhasna21.githubusers.dataclass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("login")        val username:String?,
    @SerializedName("avatar_url")   val photo_profile:String?,
    @SerializedName("blog")         val website:String?,
    val following:Long?,
    val followers:Long?,
    val name:String?,
    val location:String?,
    val company:String?,
    val email:String?,
    val bio:String?,
    val id:Int?
    ) : Parcelable

