package com.fdhasna21.githubusers.model.response

import com.google.gson.annotations.SerializedName

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

data class AllUsersResponse(
    @SerializedName("total_count") val total:Int?,
    @SerializedName("incomplete_results") val incompleteResults:Boolean?,
    @SerializedName("items") val userResponses : ArrayList<UserResponse>?
) : BaseResponse()