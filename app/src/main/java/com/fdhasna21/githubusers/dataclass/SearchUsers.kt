package com.fdhasna21.githubusers.dataclass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchUsers(
    @SerializedName("total_count") val count:Int?,
    @SerializedName("incomplete_results") val results:String?,
    @SerializedName("items") val users : ArrayList<User>?
) : Parcelable
