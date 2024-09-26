package com.fdhasna21.githubusers.model.response

import com.google.gson.annotations.SerializedName

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */
data class UserResponse(
    @SerializedName("login")        val username:String?="",
    @SerializedName("avatar_url")   val photoProfile:String?="",
    @SerializedName("blog")         val website:String?="",
    val following:Long?=0,
    val followers:Long?=0,
    val name:String?="",
    val location:String?="",
    val company:String?="",
    val email:String?="",
    val bio:String?="",
    val id:Int?=0
) : BaseResponse()