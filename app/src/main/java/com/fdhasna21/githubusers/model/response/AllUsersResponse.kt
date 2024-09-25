package com.fdhasna21.githubusers.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

@Parcelize
data class AllUsersResponse(
    @SerializedName("total_count") val total:Int?,
    @SerializedName("incomplete_results") val incompleteResults:Boolean?,
    @SerializedName("items") val userResponses : ArrayList<UserResponse>?
) : Parcelable